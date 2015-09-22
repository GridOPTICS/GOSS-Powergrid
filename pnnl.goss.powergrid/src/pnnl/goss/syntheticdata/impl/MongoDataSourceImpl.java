package pnnl.goss.syntheticdata.impl;

import java.net.UnknownHostException;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.Start;
import org.apache.felix.dm.annotation.api.Stop;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import pnnl.goss.core.server.DataSourceObject;
import pnnl.goss.core.server.DataSourceType;
import pnnl.goss.syntheticdata.api.MongoDataSource;

@Component
public class MongoDataSourceImpl implements DataSourceObject, MongoDataSource {
	private MongoClient client = null;

	public MongoDatabase getDb(){
		return client.getDatabase("powergrid");
	}

	@Start
	public void start() throws UnknownHostException{
		client = new MongoClient("localhost");
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
