package pnnl.goss.powergrid.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavePowergridResults implements Serializable{
	
	private static final long serialVersionUID = 1538240556922528221L;
	private List<String> errorsAndWarnings;
	private String powergridGuid;
	private boolean success;
	private Map<String,Object> propertyMap = new HashMap<>();
	
		
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
		propertyMap.put(key, value);
	}
	
	public void addProperty(String key, Double value){
		propertyMap.put(key, value);
	}
	
	public void addProperty(String key, Boolean value){
		propertyMap.put(key, value);
	}
	
	public void addProperty(String key, Integer value){
		propertyMap.put(key, value);
	}
}
