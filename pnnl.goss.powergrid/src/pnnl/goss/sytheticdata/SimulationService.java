package pnnl.goss.sytheticdata;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.felix.dm.annotation.api.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import pnnl.goss.sytheticdata.api.Simulator;

@Component
public class SimulationService implements Simulator {

	private Map<String, JsonObject> simulations = new ConcurrentHashMap<>();
	
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
