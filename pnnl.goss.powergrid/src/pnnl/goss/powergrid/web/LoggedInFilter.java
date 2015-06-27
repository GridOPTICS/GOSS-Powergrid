package pnnl.goss.powergrid.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.apache.felix.dm.annotation.api.Stop;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.http.HttpService;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import pnnl.goss.core.server.TokenIdentifierMap;

@Component
public class LoggedInFilter implements Filter
{

	@ServiceDependency
    private volatile ExtHttpService httpService;

	@ServiceDependency
	private volatile TokenIdentifierMap idMap;

    @Start
    public void start(){
    	System.out.println("Starting "+this.getClass().getName());
    	try {
			httpService.registerFilter(this, "/powergrid/.*",  null,  100,  null);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    @Stop
    public void stop(){
    	httpService.unregisterFilter(this);
    }

    @Override
    public void init(FilterConfig config)
        throws ServletException
    {
        doLog("Init with config [" + config + "]");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException
    {
    	HttpServletRequest httpReq = (HttpServletRequest) req;
    	String ip = httpReq.getRemoteAddr();
//    	Enumeration<String> en = httpReq.getHeaderNames();
//    	while(en.hasMoreElements()){
//    		System.out.println("en is: "+en.nextElement());
//    	}
    	String token = httpReq.getHeader("AuthToken");

    	if (token != null){
    		String user = idMap.getIdentifier(ip, token);
    		if (user != null){
    			req.setAttribute("subject", user);
    		}
    		else{
    			PrintWriter out = res.getWriter();
    			out.write("Invalid Login Attempt");
    			out.close();
    			return;
    		}
    	}
    	else{
    		StringBuilder body = new StringBuilder();
    		char[] charBuffer = new char[128];
    		InputStream inputStream = httpReq.getInputStream();
    		int bytesRead = -1;
    		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    		while ((bytesRead = reader.read(charBuffer)) > 0) {
    			body.append(charBuffer, 0, bytesRead);
    		}

    		Gson gson = new Gson();

    		JsonObject json = gson.fromJson(body.toString(), JsonObject.class);
    		token = json.get("authenticated").getAsString();

    		String user = idMap.getIdentifier(ip, token);
    		if (user != null){
    			req.setAttribute("subject", user);
    		}
    		else{
    			PrintWriter out = res.getWriter();
    			out.write("Invalid Login Attempt");
    			out.close();
    			return;
    		}
		}

        doLog("Filter request [" + req + "]");
        chain.doFilter(req, res);
    }

    public void destroy()
    {
        doLog("Destroyed filter");
    }

    private void doLog(String message)
    {
        //System.out.println("## [" + this.name + "] " + message);
    }
}