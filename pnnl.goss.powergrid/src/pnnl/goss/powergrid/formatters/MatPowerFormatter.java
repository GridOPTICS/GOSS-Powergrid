package pnnl.goss.powergrid.formatters;

import java.util.Map;

import pnnl.goss.powergrid.api.PowergridModel;
import pnnl.goss.powergrid.datamodel.Bus;

public class MatPowerFormatter implements PowergridFormatter {

	private PowergridModel model;
	
	@Override
	public String format(PowergridModel powergridModel) {		
		return format(powergridModel, null);
	}

	@Override
	public String format(PowergridModel powergridModel,
			Map<String, String> options) {
		
		model = powergridModel;
		
		StringBuilder builder = new StringBuilder();
		
		addHeading(builder, "powergrid");
		addBuses(builder);
		
		return builder.toString();
	}
	
	private void addHeading(StringBuilder builder, String functionName){
		builder.append("function mpc = "+functionName+"\n");
		// TODO Document what case it is when created etc here in the comments
		builder.append("%% MATPOWER Case Format : Version 2\n");
		builder.append("mpc.version = '2';\n\n");
		builder.append("%%-----  Power Flow Data  -----%%\n");
		builder.append("%% system MVA base\n");
		builder.append("mpc.baseMVA = "+ model.getPowergrid().getSbase().toString() +";\n\n");
	}
	
	
	private void addBuses(StringBuilder builder){
		builder.append("%% bus data\n");
		builder.append("%	bus_i	type	Pd	Qd	Gs	Bs	area	Vm	Va	baseKV	zone	Vmax	Vmin");
		builder.append("mpc.bus = [");
		for (Bus b: model.getBuses()){
			addTabDelimit(builder, b.getBusNumber(),
					b.getCode(), 
					b.getTotalPLoad(),
					b.getTotalQLoad(),
					0, // gs
					0, // b shunt
					b.getAreaNumber(),
					b.getVm(),
					b.getVa(),
					b.getBaseKv(),
					b.getZoneNumber(),
					1.05, // v-min
					0.95  // v-max
				);
			builder.append("\n");
		}		
	}
	
	private void addDelimit(StringBuilder builder,  String delimiter, Object ... items){
		boolean first = true;
		for(Object o: items){
			if (first){
				builder.append(o.toString());
				first = false;
			}
			else{
				builder.append(delimiter+o.toString());
			}
		}
	}
	private void addTabDelimit(StringBuilder builder, Object ... items){
		addDelimit(builder, "\t", items);
	}
	
	
}
