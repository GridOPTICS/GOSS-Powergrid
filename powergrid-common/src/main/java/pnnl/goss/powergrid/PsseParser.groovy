package pnnl.goss.powergrid

/**
 * Create the busbranch model from the raw input file.
 *
 * @param inDir
 * @param cards
 * @param cardMaps
 * @return
 */
def createObjects(String inDir, List cards, Map cardMaps){
    def objMap = [:]

    cards.each { card ->

        // Only deal with the cards that have the object meta data defined.
        if (cardMaps.containsKey(card)){

            def objDef = cardMaps.get(card)

            // prepare for the output of the current card's items.
            objMap[card] = []

            def cardFile = new File("${inDir}/${card}.card").readLines().each{ line ->
                def obj = new Expando()
                line.split(",").eachWithIndex { item, i ->

                    // Dynamically  create properties on the object  based upon
                    // the object's datatype and cast the value correctly.
                    if (objDef[i].datatype == int){
                        obj."${objDef[i]['field']}" = item.trim().toInteger()
                    }
                    else if(objDef[i].datatype == double){
                        obj."${objDef[i]['field']}" = item.trim().toDouble()
                    }
                    else {
                        obj."${objDef[i]['field']}" = item.replace("'", "").trim()
                    }
                }
                objMap[card] << obj
            }
        }
    }

    objMap
}


/**
 * Creates temporary .card files in an output directory for each of the passed
 * sectionCard.  This function puts the first 3 lines in a header.card file
 * in the same directory as the other cards.
 *
 * @param tempDir
 * @param rawFile
 * @param sectionCard
 * @return
 */
def createCards(String tempDir, String rawFile, List sectionCard ){
    def inputFile = new File(rawFile)
    int lineNum = 0
    String header = ""
    def currentCard = -1
    def writer = new FileWriter("${tempDir}/header.card")

    inputFile.eachLine{ line ->
        if (lineNum < 3) {
            writer.write("${line}\n")
            lineNum ++
            return
        }

        if (currentCard == -1){
            writer.close()
            currentCard = sectionCard.remove(0)
            writer = new FileWriter("${tempDir}/${currentCard}.card")
        }

        if (line.startsWith('0')){
            currentCard = sectionCard.remove(0)
            writer.close()
            writer = new FileWriter("${tempDir}/${currentCard}.card")
        }
        else{
            writer.write("${line}\n")
        }
    }
    writer.close()

}

static void main(def args){

    def cards = ['buses',
                    'generators',
                    'branches',
                    'transformer_adjustment',
                    'area',
                    'two_terminal_dc',
                    'switched_shunts',
                    'impedance_correction',
                    'multi_terminal_dc',
                    'multi_section_line',
                    'zone',
                    'inter_area_transfer',
                    'owner',
                    'facts_device']

    def tempDir = "C:/temp/data"
    createCards(tempDir, "C:/Projects/gridpack/IEEE14.raw", cards.clone())

    def busObjectDef = [
        [field: 'busNumber', outFormat: '%7d', datatype: int,
            description: "Bus numbers 1 to 999999"],
        [field: 'busType', outFormat: "'%2d'", datatype: int,
            description: "Bus Type 1) load 2) generator 3) swing 4) isolated"],
        [field: 'pLoad',		datatype: double,
            description: "Active power component of constant power in MW"],
        [field: 'qLoad',		datatype: double,
            description: "Reactive power component of constant power in MVar"],
        [field: 'gShunt',		datatype: double,
            description: "Active component of the shunt admittance to ground, entered in MW"],
        [field: 'bShunt',		datatype: double,
            description: "Reactive component of shunt admittance to ground, entered in Mvar (positive for cpacitor negative for reactor)"],
        [field: 'area',			datatype: int,
            description: "Area the load is assigned"],
        [field: 'vMag',			datatype: double,
            description: "Bus voltage magnitude, in per unit volatge"],
        [field: 'vAng',			datatype: double,
            description: "Bus voltage phase angle, in degrees"],
        [field: 'name',			datatype: String,
            description: "Alpha-numeric identifier assigned to bus"],
        [field: 'baseKv', 		datatype: double,
            description: "Bus base voltage, entered in kV"],
        [field: 'zone',			datatype: int,
            description: "Zone the load is assigned"]
    ]

    def generatorObjectDef = [
        [field: 'busNumber',	datatype: int,
            description: 'Bus number where generator is connected'],
        [field: 'machineId',	datatype: String,
            description: 'Alphanumeric machine id.  Used to distinguish between differnet machines on same bus'],
        [field: 'pGen',			datatype: double,
            description: 'Generator active power (MW)'],
        [field: 'qGen',			datatype: double,
            description: 'Generator reactive power (MVar)'],
        [field: 'qMax',			datatype: double,
            description: 'Maximum generator reactive power output (MVar)'],
        [field: 'qMin',			datatype: double,
            description: 'Minimum generator reactive power output (MVar)'],
        [field: 'vSetPoint',	datatype: double,
            description: 'Voltage setpoint; entered in pu'],
        [field: 'iRegBusNumber',datatype: double,
            description: 'Bus number of a remote type 1 or 2 bus whose voltage is to be regulated by this plant to the value specified by vSetPoint'],
        [field: 'mBase',		datatype: double,
            description: 'Total MVA base of the units represented by this machine; entered in MVA.'],
        [field: 'zSource',		datatype: double,
            description: 'Complex impedance, in pu on mBase base. '],
        [field: 'zTran',		datatype: double,
            description: 'Step-up transformer impedance; entered in pu on mBase base. '],
        [field: 'rTran',		datatype: double,
            description: 'Active components of Step-up transformer impedance, in pu on mBase base'],
        [field: 'xTran',		datatype: double,
            description: 'Reactive components of Step-up transformer impedance, in pu on mBase base'],
        [field: 'gTap',			datatype: double,
            description: 'Step-up transformer off-nominal turns ratio; entered in pu'],
        [field: 'status',		datatype: int,
            description: 'Initial machine status 1) in-service 0) out of service'],
        [field: 'rmPcnt',		datatype: double,
            description: 'Percent of the total Mvar required to hold the voltage at the bus controlled by bus that are to be contributed by the generation. It must be positive'],
        [field: 'pMax',			datatype: double,
            description: 'Maximum generator active power output; entered in MW'],
        [field: 'pMin',			datatype: double,
            description: 'Minimum generator active power output; entered in MW']
    ]

    def maps = [
        buses: busObjectDef,
        generators: generatorObjectDef
    ]


    def model = createObjects(tempDir, cards, maps)

    model.buses.each{
        println it.name
    }

    model.generators.each{
        println it.busNumber
    }

}