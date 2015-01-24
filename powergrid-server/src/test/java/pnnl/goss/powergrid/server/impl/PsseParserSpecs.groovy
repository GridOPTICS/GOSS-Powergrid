package pnnl.goss.powergrid.server.impl

import pnnl.goss.powergrid.parsers.PsseParser;
import spock.lang.Specification

class PsseParserSpecs extends Specification {

    File definitionFile = new File(
        "src/main/java/pnnl/goss/powergrid/parsers/Psse23Definitions.groovy"
    )

    File tempDirContainer = new File(
        "C:/temp/data"
    )

    File tempDir
    PsseParser parser

    def "parsing pti23 file"() {
        def rawFile = new File("C:/Projects/gridpack/IEEE14.raw")
        println definitionFile.getAbsolutePath()
        expect:
        assert definitionFile.exists()
        assert tempDir.exists()
        assert tempDir.isDirectory()
        assert rawFile.exists()

        when: "parse invoked"
        def results = parser.parse(definitionFile, tempDir, rawFile)

        then:
        assert results != null
        assert parser.model != null
        assert parser.model.buses.size() == 14
    }

    def setup(){
        tempDir = tempDirContainer.createTempDir()
        parser = new PsseParser()
    }

    def cleanup(){
        tempDir.deleteDir()
        parser = null
    }

}
