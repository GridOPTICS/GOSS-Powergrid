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
		model = powergridModel;
		StringBuilder builder = new StringBuilder();
		addHeading(builder, "powergrid");
		addBuses(builder);
		addMachines(builder);
		addBranches(builder);
		builder.append("    return ppc\n");
		return builder.toString();
	}
	private void addHeading(StringBuilder builder, String functionName){
		// TODO Document what case it is when created etc here in the comments
		builder.append(
			"from numpy import array\n\n"+
			"def "+functionName+"():\n"+
			"    '''Powerflow function for powergrid.'''\n" +
			"    ppc = {'version': '2'}\n\n"+
			"    ##-----  Power Flow Data  -----##\n"+
		    "    ## system MVA base\n"+
		    "    ppc['baseMVA'] = 100.0\n\n"
		);
	}


	private void addBuses(StringBuilder builder){
		builder.append(
		"    ## bus data\n" +
		"    # bus_i type Pd Qd Gs Bs area Vm Va baseKV zone Vmax Vmin\n" +
		"    ppc['bus'] = array([\n"
		);
		int i = 0;
		int count = model.getBuses().size();
		for (Bus b: model.getBuses()){
			builder.append("        [");
			addCommaDelimiter(builder,
					b.getBusNumber(),
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
			i = i+1;
			if (i < count){
				builder.append("],\n");
			}
			else{
				builder.append("]\n");
			}
		}

		builder.append("    ])\n\n");
	}

	private void addMachines(StringBuilder builder){
		builder.append(
			"    ## generator data\n"+
			"    # bus, Pg, Qg, Qmax, Qmin, Vg, mBase, status, Pmax, Pmin, Pc1, Pc2,\n"+
			"    # Qc1min, Qc1max, Qc2min, Qc2max, ramp_agc, ramp_10, ramp_30, ramp_q, apf\n"+
			"    ppc['gen'] = array([\n");
		int i = 0;
		int count = model.getBranches().size();
		for (Machine m: model.getMachines()){
			builder.append("        [");
			addCommaDelimiter(builder,
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
			i = i+1;
			if (i < count){
				builder.append("],\n");
			}
			else{
				builder.append("]\n");
			}
		}

		builder.append("    ])\n\n");
	}

	private void addBranches(StringBuilder builder){
		builder.append(
			"    ## branch data\n"+
			"    # fbus, tbus, r, x, b, rateA, rateB, rateC, ratio, angle, status, angmin, angmax\n"+
			"    ppc['branch'] = array([\n");
		int i = 0;
		int count = model.getBranches().size();
		for (Branch b: model.getBranches()){
			builder.append("        [");
			addCommaDelimiter(builder,
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

			i = i+1;
			if (i < count){
				builder.append("],\n");
			}
			else{
				builder.append("]\n");
			}
		}

		builder.append("    ])\n\n");

	}

	private void addDelimiter(StringBuilder builder,  String delimiter, Object ... items){
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
	private void addCommaDelimiter(StringBuilder builder, Object ... items){
		addDelimiter(builder, ",", items);
	}
}
