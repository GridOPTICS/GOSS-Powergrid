package pnnl.goss.powergrid.formatters;

import java.util.Map;

import pnnl.goss.powergrid.api.PowergridModel;

public interface PowergridFormatter {
	
	String format(PowergridModel powergridModel);
	
	String format(PowergridModel powergridModel, Map<String, String> options);
	
}
