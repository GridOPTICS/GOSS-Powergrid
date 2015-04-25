package pnnl.goss.powergrid.listeners;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.apache.felix.dm.annotation.api.Stop;
import org.apache.http.auth.UsernamePasswordCredentials;

import com.google.gson.Gson;

import pnnl.goss.core.Client;
import pnnl.goss.core.Client.PROTOCOL;
import pnnl.goss.core.ClientFactory;
import pnnl.goss.core.DataResponse;
import pnnl.goss.core.GossResponseEvent;
import pnnl.goss.core.client.ClientServiceFactory;
import pnnl.goss.powergrid.api.PowergridModel;
import pnnl.goss.powergrid.handlers.CreatePowergridHandler;
import pnnl.goss.powergrid.handlers.RequestPowergridHandler;
import pnnl.goss.powergrid.requests.CreatePowergridRequest;
import pnnl.goss.powergrid.requests.RequestPowergrid;

@Component
public class PowergridMessageStreamListener implements Runnable {
	
	@ServiceDependency
	private volatile ClientFactory clientFactory;
	private Client client = null;
	Gson gson = new Gson();
	
	static final String controlTopic = "goss/powergrid/control";
	static final String createPowergridTopci = "/topic/synthdata/request/newmodel";
	
	@Stop
	public void stop(){
		client.close();
		client = null;
		clientFactory.destroy();
	}
	
	public void setClientFactory(ClientFactory factory){
		this.clientFactory = factory;
		this.client = clientFactory.create(PROTOCOL.STOMP);
		this.client.setCredentials(new UsernamePasswordCredentials("system", "manager"));
	}
	
	class GenerationParameters {
		String username;
		String modelId;
		String profile;
		String lineOutage;
		
		public String getUserName() {
			return username;
		}
		public void setUserName(String userName) {
			this.username = userName;
		}
		public String getModel_id() {
			return modelId;
		}
		public void setModelId(String model_id) {
			this.modelId = model_id;
		}
		public String getProfile() {
			return profile;
		}
		public void setProfile(String profile) {
			this.profile = profile;
		}
		public String getLine_outage() {
			return lineOutage;
		}
		public void setLine_outage(String line_outage) {
			this.lineOutage = line_outage;
		}		
		
	}
	
	@Override
	public void run() {
        
		
		Thread thread1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					GossResponseEvent event = new GossResponseEvent(){
			               @Override
			               public void onMessage(Serializable message) {
			                     System.out.println("MESSSGE IS: " +message);
			                     CreatePowergridHandler hander = new CreatePowergridHandler();
			                     CreatePowergridRequest request = new CreatePowergridRequest();
			                     request.setPowergridName("Uploaded powergrid");
			                     request.setPowergridContent(message.toString());
			                     DataResponse response = (DataResponse)hander.handle(request);
			                     
			                     client.publishString("synthdata/poorva/newmodel/result", response.getData().toString());
	//		                     client.publish("synthdata/poorva/newmodel/result", "12");
			               }
			        };
			        
			        client.subscribeTo("/topic/synthdata/request/newmodel",event);
				}catch(Exception e){
					System.err.println(e);
				}
		        
		        
				
			}
		});
		thread1.start();
        
		Thread thread2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					GossResponseEvent event = new GossResponseEvent(){
			               @Override
			               public void onMessage(Serializable message) {
			                     System.out.println(message);
			                     
			                     GenerationParameters params = gson.fromJson((String) message, GenerationParameters.class);
			                     
			                     System.out.println("Launching job!");
			                     RequestPowergrid request = new RequestPowergrid(params.getModel_id());
			                     
			                     RequestPowergridHandler handler = new RequestPowergridHandler();
	//		                     
			                     DataResponse response = (DataResponse) handler.handle(request);
			                     PowergridModel model = (PowergridModel) response.getData();
			                     
			                     
			                     client.publish("synthdata/poorva/gendata/result", gson.toJson(model) );
			               }
			        };
			        
			        client.subscribeTo("/topic/synthdata/poorva/gendata",event);
				}catch(Exception e){
					System.err.println(e);
				}
		        
		        
				
			}
		});
		
		thread2.start();
        
        

	}
	
	public static void main(String[] args) {
		PowergridMessageStreamListener app = new PowergridMessageStreamListener();
		ClientFactory fact = new ClientServiceFactory();
		
		app.setClientFactory(new ClientServiceFactory());
		
		Thread thread = new Thread(app);
		thread.start();
		
//		Client c2 = null;
//		String data = null;
//		try(InputStream stream = new FileInputStream("/C:/Users/d3m614/git/GOSS-Powergrid/pnnl.goss.powergrid/resources/118.raw")){
//			data = IOUtils.toString(stream);
//			c2 = fact.create(PROTOCOL.STOMP);
//			c2.setCredentials(new UsernamePasswordCredentials("system", "manager"));
//			c2.publish("synthdata/request/newmodel", data);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally{
//			if (c2 != null){
//				c2.close();
//			}
//		}
//		
		//String data = IOUtils.toString(new FileInputStream("/C:/Users/d3m614/git/GOSS-Powergrid/pnnl.goss.powergrid/resources/118.raw"));
		
		
		
	}

}
