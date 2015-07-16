package pnnl.goss.powergrid.api;

import java.io.Serializable;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class SavePowergridResults implements Serializable{
	
	private static final long serialVersionUID = 1538240556922528221L;
	private List<String> errorsAndWarnings;
	private String powergridGuid;
	private boolean success;
	private JsonObject propertyMap = new JsonObject();
	
		
	public SavePowergridResults(String guid, 
			List<String> errorsAndWarnings ){
		this.success = (guid != null);
		this.powergridGuid = guid;
		this.errorsAndWarnings = errorsAndWarnings;
	}

	public List<String> getErrorsAndWarnings() {
		return errorsAndWarnings;
	}

	public String getPowergridGuid() {
		return powergridGuid;
	}

	public boolean isSuccess() {
		return success;
	}
	
	public void addProperty(String key, String value){
		propertyMap.addProperty(key, value);
	}
	
	public void addProperty(String key, Double value){
		propertyMap.addProperty(key, value);
	}
	
	public void addProperty(String key, Boolean value){
		propertyMap.addProperty(key, value);
	}
	
	public void addProperty(String key, Integer value){
		propertyMap.add(key, new JsonPrimitive(value));
	}
}
