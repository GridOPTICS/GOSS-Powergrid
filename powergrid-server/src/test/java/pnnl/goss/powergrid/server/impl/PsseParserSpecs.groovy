package pnnl.goss.powergrid.server.impl

import pnnl.goss.powergrid.parsers.ParserResultLog
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

    def "parsing pti23 IEEE14 file"() {
        def rawFile = new File("src/test/resources/IEEE14.raw")
        println definitionFile.getAbsolutePath()
        expect:
        assert definitionFile.exists()
        assert tempDir.exists()
        assert tempDir.isDirectory()
        assert rawFile.exists()

        when: "parse invoked"
        ParserResultLog results = parser.parse(definitionFile, tempDir, rawFile)

        then:
        assert results != null
        assert results.report.size() > 0
        assert parser.modelValid

        def model = parser.model
        assert model != null
        assert model.buses.size() == 14
        assert model.generators.size() == 5
        assert model.branches.size() == 20
        assert model.transformer_adjustments.size() == 0
        assert model.areas.size() == 1
        assert model.zones.size() == 1
        assert model.owners.size() == 1

        assert results.errors.size() == 0

        results.report.each{
            println it
        }
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
