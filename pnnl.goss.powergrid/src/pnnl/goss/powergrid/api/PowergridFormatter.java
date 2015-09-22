package pnnl.goss.powergrid.api;

import java.util.Map;

public interface PowergridFormatter {
	
	String format(PowergridModel powergridModel);
	
	String format(PowergridModel powergridModel, Map<String, String> options);
	
}
