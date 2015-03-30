package pnnl.goss.powergrid.server.impl

import java.io.File;

import pnnl.goss.powergrid.parsers.ResultLog
import pnnl.goss.powergrid.parsers.PsseParser;

class Psse23TestParser {


    PsseParser parser
    ResultLog results

    Psse23TestParser(File inputFile){
        File definitionFile = new File(
            "src/main/java/pnnl/goss/powergrid/parsers/Psse23Definitions.groovy"
        )

        File tempDirContainer = new File(
            "build/temp1"
        )

        File tempDir = tempDirContainer.createTempDir()

        parser = new PsseParser()
        results = parser.parse(definitionFile, tempDir, inputFile)

        tempDir.delete()
    }

}
