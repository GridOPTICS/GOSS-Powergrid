package pnnl.goss.powergrid.parser.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ParserResults implements Serializable {
	
	private static final long serialVersionUID = -8244929399372489301L;
	private final Map<String, List<PropertyGroup>> groupMap;
	private final List<String> warnings;
	private final boolean errorsFound;
	
	public ParserResults(Map<String, List<PropertyGroup>> groupMap, 
			List<String> warningAndErrors,
			boolean errorsFound){
		this.errorsFound = errorsFound;
		this.groupMap = groupMap;
		this.warnings = warningAndErrors;			
	}
	
	public boolean hasErrors(){
		return this.errorsFound;
	}
	
	public Collection<String> getErrorsAndWarnings(){
		return this.warnings;
	}
	
	public Map<String, List<PropertyGroup>> getGrouopMap(){
		return this.groupMap;
	}
}
