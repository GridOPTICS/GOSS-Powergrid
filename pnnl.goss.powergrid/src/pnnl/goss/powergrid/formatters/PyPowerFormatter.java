package pnnl.goss.powergrid.formatters;

import java.util.Map;

import pnnl.goss.powergrid.api.PowergridModel;
import pnnl.goss.powergrid.datamodel.Branch;
import pnnl.goss.powergrid.datamodel.Bus;
import pnnl.goss.powergrid.datamodel.Machine;

public class PyPowerFormatter implements PowergridFormatter {

	private PowergridModel model;
	
	@Override
	public String format(PowergridModel powergridModel) {
		return format(powergridModel, null);
	}

	@Override
	public String format(PowergridModel powergridModel,
			Map<String, String> options) {
		
		StringBuilder builder = new StringBuilder();
		
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
		builder.append("%	bus_i	type	Pd	Qd	Gs	Bs	area	Vm	Va	baseKV	zone	Vmax	Vmin\n");
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
		
		builder.append("];\n\n");		
	}
	
	private void addMachines(StringBuilder builder){
		builder.append(
			"%% generator data\n" +
			"%	bus	Pg	Qg	Qmax	Qmin	Vg	mBase	status	Pmax	Pmin	Pc1	Pc2	Qc1min	Qc1max	Qc2min	Qc2max	ramp_agc	ramp_10	ramp_30	ramp_q	apf\n"+
			"mpc.gen = [\n");
		for (Machine m: model.getMachines()){
			addTabDelimit(builder, 
					m.getBusNumber(),
					m.getPgen(),
					m.getQgen(),
					m.getMaxQgen(),
					m.getMinQgen(),
					1, // Vg
					100, // TODO should be mBase.
					m.getStatus(),
					m.getMaxPgen(),
					m.getMinPgen(),
					0,	// Pc1	
					0,  // Pc2	
					0, 	// Qc1min	
					0,	// Qc1max	
					0, 	// Qc2min	
					0,	// Qc2max	
					0,	// ramp_agc	
					0,	// ramp_10	
					0, 	// ramp_30	
					0,  // ramp_q	
					0	// apf
				);
			builder.append("\n");
		}		
		
		builder.append("];\n\n");
	}
	
	private void addBranches(StringBuilder builder){
		builder.append(
			"%% branch data\n"+
			"%	fbus	tbus	r	x	b	rateA	rateB	rateC	ratio	angle	status	angmin	angmax\n"+
			"mpc.branch = [\n");
		for (Branch b: model.getBranches()){
			addTabDelimit(builder,
				b.getFromBusNumber(),
				b.getToBusNumber(),
				b.getR(),
				b.getX(),
				b.getbCap(),
				b.getRateA(),
				b.getRateB(),
				b.getRateC(),
				b.getRatio(),
				b.getAngle(),
				b.getStatus(),
				-360, // Angle min
				360 // Angle max				
			);
			
			builder.append("\n");
		}		
		
		builder.append("];\n\n");
		
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
