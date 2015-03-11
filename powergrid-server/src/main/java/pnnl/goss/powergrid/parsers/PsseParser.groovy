package pnnl.goss.powergrid.parsers

import org.apache.commons.io.IOUtils;

class PsseParser {

    ResultLog resultLog
    def model
    def configuration
    boolean modelValid = false

    ResultLog parse(File defConfig, File tempDir, File inputFile){
        def config = null
        if (defConfig.exists()){
            config = new ConfigSlurper().parse(defConfig.text)
        } else {
            //String pathInJar = defConfig.path.replace("src/main/java/, """)
            // TODO REPLACE THIS SOONEST!
            String text = IOUtils.toString(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("pnnl/goss/powergrid/parsers/Psse23Definitions.groovy"))
            config = new ConfigSlurper().parse(text)
        }
        configuration  = config
        def cards = config.cards
        resultLog = new ResultLog()
        createTempCards(tempDir, inputFile, cards)
        model = createObjects(tempDir, cards)
        validateModel(cards)
        // True if no errors
        modelValid = resultLog.errors.size() == 0
        resultLog.successful = modelValid
        resultLog
    }

    /**
     * Validates the model based upon whether a validator has been specified
     * in the definition file.
     *
     * @param cards
     * @return
     */
    private validateModel(List cards){
        cards.each{ card ->
            if (card?.validator){
                resultLog.debug("validating ${card.name}")
                // Each object of type ${card.name}
                model[card.name].each{ obj ->obj
                    // All validators get the object to validate and the model
                    card.validator(obj, model).each { message ->
                        resultLog.error(card.name, 0, message+obj)
                    }
                }
            }
        }
    }

    /**
     * Create the busbranch model from the raw input file.
     *
     * @param inDir - A directory of cards.  Must contain a file named ${card.name}.card
     * 					for each psse section that is represented in the cards
     * 					list.
     * @param cards - A list of objects with a name and column definition attribute
     * @return
     */
    private createObjects(File inDir, List cards){
        def objMap = [:]

        cards.each { card ->

            // Only deal with the cards that have the object meta data defined.
            if (card?.columns){

                resultLog.debug("Creating ${card.name}")

                def objDef = card.columns

                // prepare for the output of the current card's items.
                objMap[card.name] = []

                def obj = null
                def hasRows = false
                // number of rows before we add the object to the list of objects
                def modCount = null

                if (objDef instanceof Map && objDef.containsKey('rows')) {
                    modCount = objDef.rows.size()
                    hasRows = true
                }


                def cardFile = new File("${inDir}/${card.name}.card").readLines().eachWithIndex{ line, lineNum ->

                    if (modCount == null || lineNum.mod(modCount) == 0){
                        obj = new Expando()
                    }

                    if (hasRows) {
                        def rowNum = lineNum % modCount
                        buildObjectFromLine (obj, line, objDef.rows[lineNum %  modCount])
                    }
                    else{
                        buildObjectFromLine (obj, line, objDef)
                    }

                    if (modCount == null || lineNum.mod(modCount) > modCount - 2) {
                        objMap[card.name] << obj
                    }
                }
            }
        }

        objMap
    }

    private buildObjectFromLine (def obj, String line, def columnDefs) {
        def splitString = ','

        if (line.split(",").size() == columnDefs.size()) {
            splitString = ','
        }
        else if (line.split(/\s+/).size() == columnDefs.size()){
            splitString = /\s+/
        }
        else {
            println "Field definition difference detected expected: ${columnDefs.size()} found ${line.split(",").size()}"
            resultLog.debug("Field definition difference detected expected: ${columnDefs.size()} found ${line.split(",").size()}")
            splitString = ','
        }
        line.trim().split(splitString).eachWithIndex { item, i ->

            if (i < columnDefs.size()) {
                // Dynamically  create properties on the object  based upon
                // the object's datatype and cast the value correctly.
                if (columnDefs[i].datatype == int){
                    try {
                        obj."${columnDefs[i]['field']}" = item.replace("'", "").trim().toInteger()
                    }
                    catch (NumberFormatException ex){
                        obj."${columnDefs[i]['field']}" = 0
                        resultLog.debug("Number format exceeption on field ${i} should be int item: ${item}")
                    }
                }
                else if(columnDefs[i].datatype == double){
                    try {
                        obj."${columnDefs[i]['field']}" = item.replace("'", "").trim().toDouble()
                    }
                    catch (NumberFormatException ex){
                        obj."${columnDefs[i]['field']}" = 0.0
                        resultLog.debug("Number format exceeption on field ${i} should be double item: ${item}")
                    }
                }
                else {
                    obj."${columnDefs[i]['field']}" = item.replace("'", "").trim()
                }
            }
            else{
                resultLog.debug("columndef not found for column ${i} on line\n'resultLog'")
            }
        }

        obj

    }


    /**
     * Creates temporary .card files in an output directory for each of the passed
     * sectionCard.  This function puts the first 3 lines in a header.card file
     * in the same directory as the other cards.
     *
     * NOTE This function will pop a card from the sectionCards list for each
     * section.  If there is not enough section cards then an error is thrown.
     *
     * @param tempDir - A directory for temp storage of cards to process.
     * @param rawFile - A base psse file to process.
     * @param cards - A processing list of cards to process.
     * @return
     */
    private createTempCards(File tempDir, File rawFile, List cards ){
        // Clone the list so we don't modify the passed list.
        List sectionCards = cards.clone()
        def inputFile = rawFile
        int lineNum = 0
        String header = ""
        def currentCard = -1
        def writer = new FileWriter("${tempDir}/header.card")
        println "Writing to: ${tempDir}/header.card"
        inputFile.eachLine{ line ->
            if (lineNum < 3) {
                writer.write("${line}\n")
                lineNum ++
                //println sprintf("%4d|%s", [lineNum, line])
                return
            }

            lineNum ++
            //println sprintf("%4d|%s", [lineNum, line])

            if (currentCard == -1){
                writer.close()
                // We remove 2 because we handle the header card specially and it
                // is now in the list of cards.
                currentCard = sectionCards.remove(0)
                currentCard = sectionCards.remove(0)
                println "Writing to: ${tempDir}/${currentCard.name}.card"
                writer = new FileWriter("${tempDir}/${currentCard.name}.card")
            }

            if (line.startsWith('0')){
                currentCard = sectionCards.remove(0)
                writer.close()
                println "Writing to: ${tempDir}/${currentCard.name}.card"
                writer = new FileWriter("${tempDir}/${currentCard.name}.card")
            }
            else{
                writer.write("${line}\n")
            }
        }
        writer.close()
    }
}
