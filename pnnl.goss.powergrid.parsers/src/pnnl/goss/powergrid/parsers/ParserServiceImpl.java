package pnnl.goss.powergrid.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.felix.dm.annotation.api.Component;

import pnnl.goss.powergrid.parser.api.InvalidDataException;
import pnnl.goss.powergrid.parser.api.ParserService;
import pnnl.goss.powergrid.parser.api.PropertyGroup;

@Component
public class ParserServiceImpl implements ParserService{

	@Override
	public Map<String, PropertyGroup> parse(String parserDefinition,
			String dataToParse) throws InvalidDataException {
		
		List<String> problems = new ArrayList<String>();
		if (dataToParse == null || dataToParse.isEmpty()){
			problems.add("Null or empty data detected.");
		}
		
		if (parserDefinition != DEFINITION_PSSE_23){
			problems.add("Invalid parser definition");			 
		}
		
		if (problems.size() > 0){
			throw new InvalidDataException(problems);
		}
		
		Map<String, PropertyGroup> map = parseData(parserDefinition, dataToParse, problems);
		
		if (problems.size() > 0){
			throw new InvalidDataException(problems);
		}
		
		return map;
	}
	
	private Map<String, PropertyGroup> parseData(String parserDefinition,
			String dataToParse, List<String> problems){
		
		Map<String, PropertyGroup> map = new ConcurrentHashMap<>();
		
		return map;
	}

}
