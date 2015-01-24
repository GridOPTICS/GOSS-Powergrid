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

    def "parsing pti23 IEEE14 file"() {
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
        def model = parser.model
        assert model != null
        assert model.buses.size() == 14
        assert model.generators.size() == 5
        assert model.branches.size() == 20
        assert model.transformer_adjustments.size() == 0
        assert model.areas.size() == 1
        assert model.zones.size() == 1
        assert model.owners.size() == 1
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
