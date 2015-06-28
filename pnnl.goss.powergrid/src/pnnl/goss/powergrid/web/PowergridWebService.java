package pnnl.goss.powergrid.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.amdatu.web.rest.doc.Description;

import pnnl.goss.core.DataResponse;
import pnnl.goss.core.Request;
import pnnl.goss.core.server.HandlerNotFoundException;
import pnnl.goss.core.server.RequestHandlerRegistry;
import pnnl.goss.powergrid.api.PowergridModel;
import pnnl.goss.powergrid.datamodel.Powergrid;
import pnnl.goss.powergrid.datamodel.collections.PowergridList;
import pnnl.goss.powergrid.requests.RequestPowergridList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Path("/powergrid/api")
public class PowergridWebService {

	private volatile RequestHandlerRegistry handlers;

	public synchronized JsonObject getRequestJsonBody(HttpServletRequest request){
		Gson gson = new Gson();
		return gson.fromJson(getRequestBody(request), JsonObject.class);
	}
	public synchronized String getRequestBody(HttpServletRequest request){
		StringBuilder body = new StringBuilder();
		char[] charBuffer = new char[128];
		InputStream inputStream;
		try {
			inputStream = request.getInputStream();
			int bytesRead = -1;
    		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    		while ((bytesRead = reader.read(charBuffer)) > 0) {
    			body.append(charBuffer, 0, bytesRead);
    		}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return body.toString();
	}

	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML})
	@Description(
		"Returns a list of Powergrid instances which contains it's mrid.  " +
		"Using the mrid the application can then post a request for a specific " +
		"PowergridModel instance or lists of it's components.")
	public Collection<Powergrid> list(String identifier, @Context HttpServletRequest request){

		System.out.println("BODY IS: "+ getRequestBody(request)); //body.toString());
		System.out.println("JSON BODY IS: " + getRequestJsonBody(request));

		System.out.println("Listing powergrids. "+ identifier);
		RequestPowergridList reqList = new RequestPowergridList();
		String subject = identifier;
		List<Powergrid> data = null;
		if (handlers.checkAccess((Request)reqList, subject)){
			DataResponse res;
			try {
				res = (DataResponse)handlers.handle(reqList);
				data = ((PowergridList)res.getData()).toList();
			} catch (HandlerNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				data = new ArrayList<>();
			}
		}
		return data;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_JSON,
		MediaType.APPLICATION_XML})
	public PowergridModel getPowergridByMrid(@Context HttpServletRequest req,
			String AuthToken){

		return null; //powergridService.getPowergridModel(mrid);

	}

//	@GET
//	@Path("myPath")
//	@Produces({ MediaType.APPLICATION_JSON })
//	@Description("Returns a friendly JSON message")
//	@Notes("This is an example")
//	@ResponseMessages({ @ResponseMessage(code = 200, message = "In case of success") })
//	@ReturnType(String.class)
//	public Response helloResponse() {
//	  return Response.ok("\"hello world\"").build();
//	}

}
