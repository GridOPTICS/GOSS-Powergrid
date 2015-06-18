package pnnl.goss.powergrid.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.http.auth.UsernamePasswordCredentials;

import pnnl.goss.core.Client;
import pnnl.goss.core.Client.PROTOCOL;
import pnnl.goss.core.ClientFactory;
import pnnl.goss.core.DataError;
import pnnl.goss.core.DataResponse;
import pnnl.goss.core.GossCoreContants;
import pnnl.goss.core.Response;
import pnnl.goss.core.client.ClientServiceFactory;
import pnnl.goss.powergrid.api.SavePowergridResults;
import pnnl.goss.powergrid.parser.api.ParserResults;
import pnnl.goss.powergrid.requests.CreatePowergridRequest;

public class PowergridMain {
	private static ClientFactory factory;
	
	private static Client getNewClient(){
		Client client = null;
		try {
			client = factory.create(PROTOCOL.OPENWIRE, 
					new UsernamePasswordCredentials("system", "manager"));
		} catch (Exception e) {
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
	public static Object createModel(String name, File file){
		Object obj = null;
		Client client = null;
		CreatePowergridRequest request = new CreatePowergridRequest();
		try {
			request.setPowergridFile(file);
			request.setPowergridName(name);
			client = getNewClient();
			Response response = client.getResponse(request);
			
			// If there wasn't an error follow this path.
			if (!handleError(response)){
				DataResponse res = (DataResponse)response;
				obj = res.getData();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally{
			client.close();
		}
		
		return obj;
	}

	public static void main(String[] args) throws Exception {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("activemq.host", "localhost");
		properties.put("openwire.port", "61616");
		properties.put("stomp.port", "61613");
		properties.put("ws.port", "61614");
		properties.put(GossCoreContants.PROP_OPENWIRE_URI, "tcp://localhost:61616");
		properties.put(GossCoreContants.PROP_STOMP_URI, "tcp://localhost:61613");
		
		factory = new ClientServiceFactory();
		((ClientServiceFactory)factory).updated(properties);
		File pgFile = new File("../pnnl.goss.powergrid.itests/resources/118.raw");
		Object obj = createModel("PSSE-118", pgFile);
		if (obj instanceof ParserResults){
			
		}
		else if(obj instanceof SavePowergridResults){
			SavePowergridResults results = (SavePowergridResults)obj;
			if (results.isSuccess()){
				System.out.println("Successful Guid: " + results.getPowergridGuid());
			}
			System.out.println("Errors or warnings");
			for(String s: results.getErrorsAndWarnings()){
				System.out.println(s);
			}
		}
		
		System.exit(0);
	}

}
