package pnnl.goss.syntheticdata.api;

import com.mongodb.client.MongoDatabase;

import pnnl.goss.core.server.DataSourceObject;

public interface MongoDataSource extends DataSourceObject {

	public MongoDatabase getDb();

}
