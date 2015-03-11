package pnnl.goss.powergrid.server.impl

import pnnl.goss.powergrid.parsers.ResultLog
import pnnl.goss.powergrid.parsers.PsseParser;
import spock.lang.Specification

class PsseParserSpecs extends Specification {

    File definitionFile = new File(
        "src/main/java/pnnl/goss/powergrid/parsers/Psse23Definitions.groovy"
    )

    File tempDirContainer = new File(
        "build/temp"
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
        ResultLog results = parser.parse(definitionFile, tempDir, rawFile)

        then:
        assert results != null
        assert results.log.size() > 0
        assert parser.modelValid

        def model = parser.model
        assert model != null
        assert parser.configuration.fileType == "Psse23"

        assert model.header.size() == 1
        assert model.header[0].ic == 0
        assert model.header[0].sbase == 100.0
        assert model.header[0].desc1 == 'A'
        assert model.header[0].desc2 == 'B'
        assert model.buses.size() == 14
        assert model.generators.size() == 5
        assert model.branches.size() == 20
        def branch1 = model.branches[0]
        assert branch1.r == 0.01938
        assert branch1.x == 0.05917
        assert branch1.b == 0.05280
        assert branch1.status == 1
        assert branch1.ckt == 'BL'
        assert branch1.fromBus == 1
        assert branch1.toBus == 2
        assert model.transformer_adjustments.size() == 0
        assert model.areas.size() == 1
        assert model.zones.size() == 1
        assert model.owners.size() == 1

        assert results.errors.size() == 0

        results.log.each{
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
