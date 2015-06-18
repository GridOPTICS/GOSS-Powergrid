package pnnl.goss.powergrid.parser.api;

import java.io.InputStream;

public interface ParserService {
	
	/**
	 * The parser service will allow the passing of an inputstream.  Depending upon the
	 * value of the parserDefinition the function should route to either a line readable
	 * model parser (psse/pti file) or a stream based (rdf) parser.
	 * 
	 * @param parserDefinition - A string representing the definition file to use. 
	 * @param dataStream - A stream of data to be parsed.
	 * @return - A {@link ParserResults} object.
	 * @throws InvalidDataException 
	 */
	ParserResults parse(String parserDefinition, InputStream dataStream) throws InvalidDataException;
	
//	/**
//	 * Lookup the available definition files and return them.
//	 * 
//	 * @return
//	 */
//	List<String> getAvailableDefinitions();

}
