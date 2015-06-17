package pnnl.goss.powergrid.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.ConfigurationException;

import org.apache.http.auth.UsernamePasswordCredentials;

import pnnl.goss.core.Client;
import pnnl.goss.core.Client.PROTOCOL;
import pnnl.goss.core.ClientFactory;
import pnnl.goss.core.DataError;
import pnnl.goss.core.DataResponse;
import pnnl.goss.core.GossCoreContants;
import pnnl.goss.core.Response;
import pnnl.goss.core.client.ClientServiceFactory;
import pnnl.goss.powergrid.requests.CreatePowergridRequest;

public class PowergridMain {
	private static ClientFactory factory;
	
	private static Client getNewClient(){
		Client client = null;
		try {
			client = factory.create(PROTOCOL.OPENWIRE, 
					new UsernamePasswordCredentials("system", "manager"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return client;
	}
	
	public static boolean handleError(Response response){
		
		if (response instanceof DataResponse){
			DataResponse res = (DataResponse) response;
			if(res.getData() instanceof DataError){
				System.err.println("ERROR: "+ ((DataError)res.getData()).getMessage());
				return true;
			}
		}
		
		return false;
	}
	public static String createModel(String name, File file){
		String guid = null;
		
		CreatePowergridRequest request = new CreatePowergridRequest();
		try {
			request.setFile(file);
			request.setPowergridName(name);
			Client client = getNewClient();
			Response response = client.getResponse(request);
			
			// If there wasn't an error follow this path.
			if (!handleError(response)){
				DataResponse res = (DataResponse)response;
				guid = (String)res.getData();
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return guid;
	}

	public static void main(String[] args) throws Exception {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("activemq.host", "localhost");
		properties.put("openwire.port", "61616");
		properties.put("stomp.port", "61613");
		properties.put("ws.port", "61614");
		properties.put(GossCoreContants.PROP_OPENWIRE_URI, "tcp://localhost:61616");
		properties.put(GossCoreContants.PROP_STOMP_URI, "tcp://localhost:61613");
		
		// TODO Auto-generated method stub
		factory = new ClientServiceFactory();
		((ClientServiceFactory)factory).updated(properties);
		File pgFile = new File("../pnnl.goss.powergrid.itests/resources/118.raw");
		String guid = createModel("PSSE-118", pgFile);
		System.out.println("Guid is: "+guid);
		//System.out.println(pgFile.getAbsolutePath()+ " exists? "+ pgFile.exists());
		//String result = createModel("PSSE-123"), filename)
	}

}
