package pnnl.goss.syntheticdata.impl;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.bson.BsonDateTime;
import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoDatabase;

import pnnl.goss.core.server.DataSourceRegistry;
import pnnl.goss.syntheticdata.api.MongoDataSource;
import pnnl.goss.syntheticdata.api.SimulatorService;

@Component
public class SimulationServiceImpl implements SimulatorService {

	private Map<String, JsonObject> simulations = new ConcurrentHashMap<>();

	@ServiceDependency
	private volatile DataSourceRegistry datasourceRegistry;

	private Gson gson = new Gson();

	@Override
	public String runSimulation(String jsonSimulationOptions) {

		JsonObject simulationArgs = gson.fromJson(jsonSimulationOptions, JsonObject.class);

		JsonObject wrapper = new JsonObject();
		String simToken = UUID.randomUUID().toString();
		wrapper.addProperty("simulationToken", simToken);
		wrapper.add("initialParameters", simulationArgs);
		wrapper.addProperty("startTime", new Date().getTime());
		wrapper.addProperty("endTime", "");

		simulations.put(simToken, wrapper);

		// TODO Start running thread for the simulation with an event queue for updating.

		MongoDataSource datasource = (MongoDataSource)datasourceRegistry.get("mongodbdata");

		MongoDatabase db = datasource.getDb();

		Document doc = new Document();

		doc.append("simulation_key", simToken);
		doc.append("timestamp_hour", new BsonDateTime(wrapper.get("startTime").getAsLong()));

		db.getCollection("simulations").insertOne(doc);

		return  simToken;
	}

	@Override
	public String getStatus(String simulationToken) {

		JsonObject wrapped = simulations.get(simulationToken);

		if (wrapped != null){
			return gson.toJson(wrapped);
		}

		wrapped = new JsonObject();

		wrapped.addProperty("error", "Unknown simulation token: "+ simulationToken);

		return gson.toJson(wrapped);
	}



}
