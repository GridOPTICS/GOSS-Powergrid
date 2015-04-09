package pnnl.goss.powergrid.parsers;

import java.util.Arrays;
import java.util.Collections;

import pnnl.goss.powergrid.parser.api.InvalidDataException;

public class ColumnMeta {
	
	private String field;
	private String outputFormat;
	private String dataType;
	private String description;
	
	public ColumnMeta() {}
	public ColumnMeta(ColumnMeta meta){
		// Copy ok because Strings are immutable
		field = meta.field;
		outputFormat = meta.outputFormat;
		dataType = meta.dataType;
		description = meta.description;
	}
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getOutputFormat() {
		return outputFormat;
	}
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns the class associated with Integer, Double, or String
	 * based upon what is in the dataType field.
	 * 
	 * @return
	 * @throws InvalidDataException
	 */
	@SuppressWarnings("rawtypes")
	public Class getPrimativeType() throws InvalidDataException{
		if (dataType.toLowerCase().equals("int")){
			return Integer.class;
		}
		else if(dataType.toLowerCase().equals("double")){
			return Double.class;
		}
		else if(dataType.toLowerCase().equals("string")){
			return Double.class;
		}
		
		throw new InvalidDataException(Arrays.asList(new String[]{"invalid dataType!"}));
	}
}
