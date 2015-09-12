package pnnl.goss.powergrid.server.mongodb;


import java.net.UnknownHostException;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.Property;
import org.apache.felix.dm.annotation.api.Start;
import org.apache.felix.dm.annotation.api.Stop;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import pnnl.goss.core.server.DataSourceObject;
import pnnl.goss.core.server.DataSourceType;

@Component
public class MongoDataSource implements DataSourceObject {
	
	private MongoClient client = null;
	
	public MongoDatabase getDb(){
		return client.getDatabase("powergrid");
	}
	
	@Start
	public void start() throws UnknownHostException{
		client = new MongoClient("eioc-goss.pnl.gov");
	}
	
	@Stop
	public void stop(){
		if (client != null){
			client.close();
		}
	}
	
	@Override
	public String getName() {
		return "mongodbdata";
	}

	@Override
	public DataSourceType getDataSourceType() {
		return DataSourceType.DS_TYPE_OTHER;
	}
	
	

}
