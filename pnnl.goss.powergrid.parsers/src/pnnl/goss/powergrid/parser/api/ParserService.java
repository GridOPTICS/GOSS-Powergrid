package pnnl.goss.powergrid.parser.api;

import java.util.List;
import java.util.Map;

public interface ParserService {
	
	/**
	 * The parser is parser the passed data (pti file data, cim file data) and return
	 * a list of properties that are specified on the definition file of the definition
	 * file.
	 * 
	 * If the parsed data is not valid then InvalidDataException will be thrown.
	 *  
	 * @param parserDefinition - A string representing the definition file to use.
	 * @param dataToParse - A string of file data to be passed to the parser.
	 * @return - A list of property groups that are specified in the definition file.
	 * @throws InvalidDataException 
	 */
	Map<String, PropertyGroup> parse(String parserDefinition, String dataToParse) throws InvalidDataException;
	
	/**
	 * Lookup the available definition files and return them.
	 * 
	 * @return
	 */
	List<String> getAvailableDefinitions();

}
