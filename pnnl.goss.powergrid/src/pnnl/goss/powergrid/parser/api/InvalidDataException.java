package pnnl.goss.powergrid.parser.api;

import java.util.List;

public class InvalidDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 888560277504156527L;
	
	private List<String> messages;
	
	public InvalidDataException(List<String> invalidMessageData){
		super();
		this.messages = invalidMessageData;
	}
	
	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder();
		for(String s:messages){
			builder.append(s);
			builder.append("\n");
		}

		return builder.toString();
	}
	

}
