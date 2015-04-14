package pnnl.goss.powergrid.parser.api;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface ParserService {
	
	/**
	 * The parser service will allow the passing of an inputstream.  Depending upon the
	 * value of the parserDefinition the function should route to either a line readable
	 * model parser (psse/pti file) or a stream based (rdf) parser.
	 * 
	 * If the parsed data is not valid then InvalidDataException will be thrown.
	 *  
	 * @param parserDefinition - A string representing the definition file to use.
	 * @param dataStream - A stream of data to be parsed.
	 * @return - A map of string (class type/card types) with a List of property group values. An
	 * 				individual PropertyGroup represents a single object in the model.
	 * @throws InvalidDataException 
	 */
	Map<String, List<PropertyGroup>> parse(String parserDefinition, InputStream dataStream) throws InvalidDataException;
	
	/**
	 * Lookup the available definition files and return them.
	 * 
	 * @return
	 */
	List<String> getAvailableDefinitions();

}
