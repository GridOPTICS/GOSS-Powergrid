package pnnl.goss.powergrid.parsers

class PsseParser {

    ParserResultLog resultLog
    def model
    def modelValid = false

    ParserResultLog parse(File defConfig, File tempDir, File inputFile){
        def config = new ConfigSlurper().parse(defConfig.text)
        def cards = config.cards
        resultLog = new ParserResultLog()
        createTempCards(tempDir, inputFile, cards)
        model = createObjects(tempDir, cards)
        validateModel(cards)
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
            println card.name

            // Only deal with the cards that have the object meta data defined.
            if (card?.columns){

                def objDef = card.columns

                // prepare for the output of the current card's items.
                objMap[card.name] = []

                def cardFile = new File("${inDir}/${card.name}.card").readLines().each{ line ->
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

                    objMap[card.name] << obj
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
                currentCard = sectionCards.remove(0)
                writer = new FileWriter("${tempDir}/${currentCard.name}.card")
            }

            if (line.startsWith('0')){
                currentCard = sectionCards.remove(0)
                writer.close()
                writer = new FileWriter("${tempDir}/${currentCard.name}.card")
            }
            else{
                writer.write("${line}\n")
            }
        }
        writer.close()
    }
}
