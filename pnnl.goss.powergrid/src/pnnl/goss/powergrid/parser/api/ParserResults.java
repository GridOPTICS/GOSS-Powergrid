package pnnl.goss.powergrid.parser.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ParserResults implements Serializable {
	
	private static final long serialVersionUID = -8244929399372489301L;
	private final List<String> warnings;
	private final boolean errorsFound;
	private final String[] sections;
	private final JsonObject sectionMap;
	
	public ParserResults(JsonObject parsedData,
			String[] sections, 
			List<String> warningAndErrors,
			boolean errorsFound){
		this.errorsFound = errorsFound;
		this.sections = sections;
		this.sectionMap = parsedData;
		this.warnings = warningAndErrors;			
	}
	
	public JsonObject getSectionMap(){
		return sectionMap;
	}
	
	public boolean hasErrors(){
		return this.errorsFound;
	}
	
	public Collection<String> getErrorsAndWarnings(){
		return this.warnings;
	}
	
	public String[] getSections(){
		return sections;
	}
	
	public JsonArray getData(String section){
		return sectionMap.get(section).getAsJsonObject().get("data").getAsJsonArray();
	}
	
	public JsonArray getType(String section){
		return sectionMap.get(section).getAsJsonObject().get("type_order").getAsJsonArray();
	}
	
	public JsonArray getPtiNames(String section){
		return sectionMap.get(section).getAsJsonObject().get("names").getAsJsonArray();
	}
	
}
