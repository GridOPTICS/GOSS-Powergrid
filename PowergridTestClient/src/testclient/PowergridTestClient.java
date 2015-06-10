package testclient;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;

import pnnl.goss.core.Client;
import pnnl.goss.core.Client.PROTOCOL;
import pnnl.goss.core.DataResponse;
import pnnl.goss.core.Response;
import pnnl.goss.core.client.ClientServiceFactory;
import pnnl.goss.powergrid.requests.CreatePowergridRequest;

public class PowergridTestClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientServiceFactory factory = new ClientServiceFactory();
		
		factory.setOpenwireUri("tcp://localhost:6011");
		
		Credentials creds = new UsernamePasswordCredentials("system", "manager");
		Client client = null;
		try {
			client = factory.create(PROTOCOL.OPENWIRE, creds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (client != null){
			doCreatePowergridRequest(client);
			client.close();
		}
	}
	
	private static void printDataResponse(Response response){
		if(response instanceof DataResponse){
			DataResponse dataResponse = (DataResponse) response;
			if (dataResponse.getData() != null){
				System.out.println("DataResponse: "+ dataResponse.getData().toString());
			}
			else{
				System.err.println("DataResponse was null!");
			}
		}
		else{
			System.err.println("Response is not DataResponse");
		}
	}
	
	private static void doCreatePowergridRequest(Client client){
		System.out.println("Doing CreatePowergridRequest");
		CreatePowergridRequest request = new CreatePowergridRequest();
		System.out.println((new File(".")).getAbsolutePath());
		File file = new File("../pnnl.goss.powergrid/resources/118.raw");
				
		try {
			request.setFile(file);
			Response response = client.getResponse(request);
			printDataResponse(response);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
