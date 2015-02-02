name = "Psse23 Column Definition File"
description = """
This configuration file was created with the help of the IEEE14 bus model
and the information located at: https://www.ee.washington.edu/research/pstca/formats/pti.txt.

The assumption that it is accurate however that can not be guaranteed.
"""

busColumnsDef = [
    [field: 'busNumber', outFormat: '%7d', datatype: int,
        description: "Bus numbers 1 to 999999"],
    [field: 'busType', outFormat: "'%2d'", datatype: int,
        description: "Bus Type 1) load 2) generator 3) swing 4) isolated"],
    [field: 'pLoad', datatype: double,
        description: "Active power component of constant power in MW"],
    [field: 'qLoad', datatype: double,
        description: "Reactive power component of constant power in MVar"],
    [field: 'gShunt', datatype: double,
        description: "Active component of the shunt admittance to ground, entered in MW"],
    [field: 'bShunt', datatype: double,
        description: "Reactive component of shunt admittance to ground, entered in Mvar (positive for cpacitor negative for reactor)"],
    [field: 'area',	datatype: int,
        description: "Area the load is assigned"],
    [field: 'vMag',	datatype: double,
        description: "Bus voltage magnitude, in per unit volatge"],
    [field: 'vAng',	datatype: double,
        description: "Bus voltage phase angle, in degrees"],
    [field: 'name',	datatype: String,
        description: "Alpha-numeric identifier assigned to bus"],
    [field: 'baseKv', datatype: double,
        description: "Bus base voltage, entered in kV"],
    [field: 'zone',	datatype: int,
        description: "Zone the load is assigned"]
]

generatorColumnsDef = [
    [field: 'busNumber', datatype: int,
        description: 'Bus number where generator is connected'],
    [field: 'machineId', datatype: String,
        description: 'Alphanumeric machine id.  Used to distinguish between differnet machines on same bus'],
    [field: 'pGen',	datatype: double,
        description: 'Generator active power (MW)'],
    [field: 'qGen', datatype: double,
        description: 'Generator reactive power (MVar)'],
    [field: 'qMax',	datatype: double,
        description: 'Maximum generator reactive power output (MVar)'],
    [field: 'qMin',	datatype: double,
        description: 'Minimum generator reactive power output (MVar)'],
    [field: 'vSetPoint', datatype: double,
        description: 'Voltage setpoint; entered in pu'],
    [field: 'iRegBusNumber',datatype: int,
        description: 'Bus number of a remote type 1 or 2 bus whose voltage is to be regulated by this plant to the value specified by vSetPoint'],
    [field: 'mBase', datatype: double,
        description: 'Total MVA base of the units represented by this machine; entered in MVA.'],
    [field: 'zSource', datatype: double,
        description: 'Complex impedance, in pu on mBase base. '],
    [field: 'zTran', datatype: double,
        description: 'Step-up transformer impedance; entered in pu on mBase base. '],
    [field: 'rTran', datatype: double,
        description: 'Active components of Step-up transformer impedance, in pu on mBase base'],
    [field: 'xTran', datatype: double,
        description: 'Reactive components of Step-up transformer impedance, in pu on mBase base'],
    [field: 'gTap',	datatype: double,
        description: 'Step-up transformer off-nominal turns ratio; entered in pu'],
    [field: 'status', datatype: int,
        description: 'Initial machine status 1) in-service 0) out of service'],
    [field: 'rmPcnt', datatype: double,
        description: 'Percent of the total Mvar required to hold the voltage at the bus controlled by bus that are to be contributed by the generation. It must be positive'],
    [field: 'pMax', datatype: double,
        description: 'Maximum generator active power output; entered in MW'],
    [field: 'pMin',	datatype: double,
        description: 'Minimum generator active power output; entered in MW']
]

branchColumnsDef = [
    [field: 'fromBus', datatype: int, description: ''],
    [field: 'toBus', datatype: int, description: ''],
    [field: 'ckt', datatype: String, description: ''],
    [field: 'r', datatype: double, description: ''],
    [field: 'x', datatype: double, description: ''],
    [field: 'b', datatype: double, description: ''],
    [field: 'ratingA', datatype: double, description: ''],
    [field: 'ratingB', datatype: double, description: ''],
    [field: 'ratingC', datatype: double, description: ''],
    [field: 'ratio', datatype: double, description: ''],
    [field: 'angle', datatype: double, description: ''],
    [field: 'shuntG1', datatype: double, description: ''],
    [field: 'shuntB1', datatype: double, description: ''],
    [field: 'shuntG2', datatype: double, description: ''],
    [field: 'shuntB2', datatype: double, description: ''],
    [field: 'status', datatype: int, description: ''],
]

transformerAdjustmentColumnsDef = [
    [field: 'fromBus', datatype: int, description: ''],
    [field: 'toBus', datatype: int, description: ''],
    [field: 'ckt', datatype: String, description: ''],
    [field: 'control', datatype: int, description: ''],
    [field: 'rma', datatype: double, description: ''],
    [field: 'rmi', datatype: double, description: ''],
    [field: 'vma', datatype: double, description: ''],
    [field: 'vmi', datatype: double, description: ''],
    [field: 'step', datatype: double, description: ''],
    [field: 'table', datatype: int, description: '']
]

areaColumnsDef = [
    [field: 'areaNumber', datatype: int, description: ''],
    [field: 'isw', datatype: int, description: ''],
    [field: 'pDesired', datatype: double, description: ''],
    [field: 'pTolerance', datatype: double, description: ''],
    [field: 'name', datatype: String, description: ''],
]

switchedShuntColumnsDef = [
    [field: 'busNumber', datatype: int, description: ''],
    [field: 'controlMode', datatype: int, description: ''],
    [field: 'vswHi', datatype: double, description: ''],
    [field: 'vswLo', datatype: double, description: ''],
    [field: 'swRem', datatype: int, description: ''],
    [field: 'bInit', datatype: int, description: ''],
    [field: 'n1', datatype: int, description: ''],
    [field: 'b1', datatype: double, description: ''],
    [field: 'n2', datatype: int, description: ''],
    [field: 'b2', datatype: double, description: ''],
    [field: 'n3', datatype: int, description: ''],
    [field: 'b3', datatype: double, description: ''],
    [field: 'n4', datatype: int, description: ''],
    [field: 'b4', datatype: double, description: ''],
    [field: 'n5', datatype: int, description: ''],
    [field: 'b5', datatype: double, description: ''],
    [field: 'n6', datatype: int, description: ''],
    [field: 'b6', datatype: double, description: ''],
    [field: 'n7', datatype: int, description: ''],
    [field: 'b7', datatype: double, description: ''],
    [field: 'n8', datatype: int, description: ''],
    [field: 'b8', datatype: double, description: '']
]

def zoneColumnsDef = [
    [field: 'zoneNumber', datatype: int, description: ''],
    [field: 'name', datatype: String, description: '']
]

def ownerColumnsDef = [
    [field: 'ownerNumber', datatype: int, description: ''],
    [field: 'name', datatype: String, description: '']
]

/**
 * Validator of branch object.
 *
 * Requires that branch elements are in the buses object.
 */
def branchValidator = { branch, model ->
    def hasFrom = false
    def hasTo = false
    def buses = model.buses
    def messages = []

    buses.each{ bus1 ->
        if (bus1.busNumber == branch.fromBus) {
            hasFrom = true
        }
        if (bus1.busNumber == branch.toBus) {
            hasTo = true
        }
    }
    if (!(hasFrom && hasTo)){
        messages << "Bus number is not found in buses"
    }
    else{
        messages
    }
}

// Cards are ordered and are managed based upon the column def
cards = [
    [name: 'buses', columns: busColumnsDef],
    [name: 'generators', columns: generatorColumnsDef],
    [name: 'branches', columns: branchColumnsDef, validator: branchValidator],
    [name: 'transformer_adjustments', columns: transformerAdjustmentColumnsDef],
    [name: 'areas', columns: areaColumnsDef],
    [name: 'two_terminal_dc'],
    [name: 'switched_shunts'],
    [name: 'impedance_corrections'],
    [name: 'multi_terminal_dc'],
    [name: 'multi_section_line'],
    [name: 'zones', columns: zoneColumnsDef],
    [name: 'inter_area_transfers'],
    [name: 'owners', columns: ownerColumnsDef],
    [name: 'facts_device']

]