package pnnl.goss.powergrid.parser.api;

import java.io.Serializable;

public class Property implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6772234004943303691L;

	/**
	 * The name of the property.
	 */
	private String name;
	
	/**
	 * Value of the object.
	 */
	private Object value;
	
	/**
	 * The datatype of the value.
	 */
	private String dataType;

	public String getName() {
		return name;
	}

	public Property setName(String name) {
		this.name = name;
		return this;
	}

	public Object getValue() {
		return value;
	}

	public Property setValue(Object value) {
		this.value = value;
		return this;
	}

	public String getDataType() {
		return dataType;
	}

	public Property setDataType(String dataType) {
		this.dataType = dataType;
		return this;
	}
	
	public Property set(String name, String dataType, Object value){
		return this.setDataType(dataType)
				.setName(name)
				.setValue(value);
	}
}
