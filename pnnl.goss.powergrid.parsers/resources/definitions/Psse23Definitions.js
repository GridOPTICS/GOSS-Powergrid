var definition = {
	name : "Psse23 Column Definition File",
	fileType : "Psse23",
	description : [
			"This configuration file was created with the help of the IEEE14 bus model ",
			"and the information located at: https://www.ee.washington.edu/research/pstca/formats/pti.txt.",
			"The assumption that it is accurate however that can not be guaranteed." ]
			.join(" \n")
};

function column(field, dataType, description, outputFormat) {
	this.field = field;
	this.outputFormat = outputFormat;
	this.dataType = dataType;
	this.description = description;
}

var busColumns = [
		new column('busNumber', "int", "Bus numbers 1 to 999999", '%7d'),
		new column('busType', 'int',
				"Bus Type 1) load 2) generator 3) swing 4) isolated", "'%2d'"),
		new column('pLoad', 'double',
				"Active power component of constant power in MW"),
		new column('qLoad', 'double',
				"Reactive power component of constant power in MVar"),
		new column('gShunt', 'double',
				"Active component of the shunt admittance to ground, entered in MW"),
		new column(
				'bShunt',
				'double',
				"Reactive component of shunt admittance to ground, entered in Mvar (positive for cpacitor negative for reactor)"),
		new column('area', 'int', "Area the load is assigned"),
		new column('vMag', 'double',
				"Bus voltage magnitude, in per unit volatge"),
		new column('vAng', 'double', "Bus voltage phase angle, in degrees"),
		new column('name', 'String', "Alpha-numeric identifier assigned to bus"),
		new column('baseKv', 'double', "Bus base voltage, entered in kV"),
		new column('zone', 'int', "Zone the load is assigned") ];

for (var i = 0; i < busColumns.length; i++) {
	print(busColumns[i].field);
}
var generatorColumnsDef = [
		new column('busNumber', 'int',
				'Bus number where generator is connected'),
		new column(
				'machineId',
				'String',
				'Alphanumeric machine id.  Used to distinguish between differnet machines on same bus'),
		new column('pGen', 'double', 'Generator active power (MW)'),
		new column('qGen', 'double', 'Generator reactive power (MVar)'),
		new column('qMax', 'double',
				'Maximum generator reactive power output (MVar)'),
		new column('qMin', 'double',
				'Minimum generator reactive power output (MVar)'),
		new column('vSetPoint', 'double', 'Voltage setpoint; entered in pu'),
		new column(
				'iRegBusNumber',
				'int',
				'Bus number of a remote type 1 or 2 bus whose voltage is to be regulated by this plant to the value specified by vSetPoint'),
		new column('mBase', 'double',
				'Total MVA base of the units represented by this machine; entered in MVA.'),
		new column('zSource', 'double',
				'Complex impedance, in pu on mBase base. '),
		new column('zTran', 'double',
				'Step-up transformer impedance; entered in pu on mBase base. '),
		new column('rTran', 'double',
				'Active components of Step-up transformer impedance, in pu on mBase base'),
		new column('xTran', 'double',
				'Reactive components of Step-up transformer impedance, in pu on mBase base'),
		new column('gTap', 'double',
				'Step-up transformer off-nominal turns ratio; entered in pu'),
		new column('status', 'int',
				'Initial machine status 1) in-service 0) out of service'),
		new column(
				'rmPcnt',
				'double',
				'Percent of the total Mvar required to hold the voltage at the bus controlled by bus that are to be contributed by the generation. It must be positive'),
		new column('pMax', 'double',
				'Maximum generator active power output; entered in MW'),
		new column('pMin', 'double',
				'Minimum generator active power output; entered in MW') ];

var branchColumnsDef = [
		new column('fromBus', 'int', 'From bus number'),
		new column('toBus', 'int', 'To bus number'),
		new column('ckt', 'String',
				'Circuit identifier two character uppercase alphanumeric'),
		new column('r', 'double', 'Branch resistents (pu)'),
		new column('x', 'double', 'Branch reactance (pu)'),
		new column('b', 'double', 'Total branch charging susceptance (pu)'),
		new column('ratingA', 'double', 'First rating (MVA)'),
		new column('ratingB', 'double', 'Second rating (MVA)'),
		new column('ratingC', 'double', 'Third rating (MVA)'),
		new column('ratio', 'double', 'Transformer off nominal turns ratio.'),
		new column('angle', 'double', 'Transformer phase shift angle.'),
		new column('shuntG1', 'double',
				'Real portion of complex admittance of the line shunt at the fromBus (pu)'),
		new column('shuntB1', 'double',
				'Imaginary portion of complex admittance of the line shunt at the fromBus (pu)'),
		new column('shuntG2', 'double',
				'Real portion of complex admittance of the line shunt at the toBus (pu)'),
		new column('shuntB2', 'double',
				'Imaginary portion of complex admittance of the line shunt at the toBus (pu)'),
		new column('status', 'int', 'In service status of the branch.') ];

var transformerAdjustmentColumnsDef = [ new column('fromBus', 'int', ''),
		new column('toBus', 'int', ''), new column('ckt', 'String', ''),
		new column('control', 'int', ''), new column('rma', 'double', ''),
		new column('rmi', 'double', ''), new column('vma', 'double', ''),
		new column('vmi', 'double', ''), new column('step', 'double', ''),
		new column('table', 'int', '') ];

var areaColumnsDef = [ new column('areaNumber', 'int', ''),
		new column('isw', 'int', ''), new column('pDesired', 'double', ''),
		new column('pTolerance', 'double', ''),
		new column('name', 'String', '') ];

var switchedShuntColumnsDef = [ new column('busNumber', 'int', ''),
		new column('controlMode', 'int', ''),
		new column('vswHi', 'double', ''), new column('vswLo', 'double', ''),
		new column('swRem', 'int', ''), new column('bInit', 'int', ''),
		new column('n1', 'int', ''), new column('b1', 'double', ''),
		new column('n2', 'int', ''), new column('b2', 'double', ''),
		new column('n3', 'int', ''), new column('b3', 'double', ''),
		new column('n4', 'int', ''), new column('b4', 'double', ''),
		new column('n5', 'int', ''), new column('b5', 'double', ''),
		new column('n6', 'int', ''), new column('b6', 'double', ''),
		new column('n7', 'int', ''), new column('b7', 'double', ''),
		new column('n8', 'int', ''), new column('b8', 'double', '') ];

var zoneColumnsDef = [ new column('zoneNumber', 'int', ''),
		new column('name', 'String', '') ];

var ownerColumnsDef = [ new column('ownerNumber', 'int', ''),
		new column('name', 'String', '') ];

/**
 * Validator of branch object.
 *
 * Requires that branch elements are in the buses object.
 */
var branchValidator = function (branch, model){
	this.hasFrom = false;
	this.hasTo = false;
	this.buses = model.buses;
	this.messages = [];
	
	for(var i=0; i<this.buses.length; i++){
		var bus = this.buses[i];
		
		if (bus.busNumber == branch.fromBus){
			this.hasFrom = true;
		}
		
		if (bus.busNumber == branch.toBus){
			this.hasTo = true;
		}
		
		if (this.hasTo && this.hasFrom){
			break;
		}
	}
	
	return (this.hasTo && this.hasFrom);
}

function card(name, columns, validator){
	this.name = name;
	this.columns = columns;
	this.validator = validator;
}


// Cards are ordered and are managed based upon the column def
var cards = [
    new card('buses', busColumns),
    new card('generators', generatorColumnsDef),
    new card('branches', branchColumnsDef, branchValidator),
    new card('transformer_adjustments', transformerAdjustmentColumnsDef),
    new card('areas', areaColumnsDef),
    new card('two_terminal_dc'),
    new card('switched_shunts', switchedShuntColumnsDef),
    new card('impedance_corrections'),
    new card('multi_terminal_dc'),
    new card('multi_section_line'),
    new card('zones', zoneColumnsDef),
    new card('inter_area_transfers'),
    new card('owners', ownerColumnsDef),
    new card('facts_device')
]

var cardModel = JSON.stringify(cards);

print("Done transforming!");
print(JSON.stringify(cards));