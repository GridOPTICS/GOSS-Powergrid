package pnnl.goss.powergrid.listeners;

import java.io.Serializable;
import java.text.ParseException;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.apache.felix.dm.annotation.api.Stop;

import pnnl.goss.core.Client;
import pnnl.goss.core.Client.PROTOCOL;
import pnnl.goss.core.ClientFactory;
import pnnl.goss.core.DataResponse;
import pnnl.goss.core.GossResponseEvent;

@Component
public class PowergridMessageStreamListener {
	
	@ServiceDependency
	private volatile ClientFactory clientFactory;
	private Client client = null;
	
	static final String controlTopic = "goss/powergrid/control";
	static final String createPowergridTopci = "/topic/synthdata/request/newmodel";
	
	@Stop
	public void stop(){
		client.close();
		client = null;
		clientFactory.destroy();
	}
	

	//@Override
	public void run() {
        
        GossResponseEvent event = new GossResponseEvent(){
               @Override
               public void onMessage(Serializable message) {
                     System.out.println(message);
                     client.publish("synthdata/poorva/newmodel/result", "12");
               }
        };
        
        client.subscribeTo("/topic/synthdata/request/newmodel",event);
        
        event = new GossResponseEvent(){
               @Override
               public void onMessage(Serializable message) {
                     System.out.println(message);
                     client.publish("synthdata/poorva/gendata/result", "xml file");
               }
        };
        
        client.subscribeTo("/topic/synthdata/poorva/gendata",event);

	}

}
