package pnnl.goss.powergrid.server.impl

import pnnl.goss.powergrid.PowergridCreationReport;
import pnnl.goss.powergrid.server.impl.PowergridServiceImpl
import spock.lang.Specification

class PowergridServiceImplSpecs extends Specification {
    PowergridServiceImpl pgService

    def setup(){
        pgService = new PowergridServiceImpl()
    }

    def "can create powergrid from pti23 IEEE14 bus file"(){
        File ieee14File = new File("src/test/resources/IEEE14.raw")
        expect:
        ieee14File.exists()

        when: "Calling the service with an existing file."
        PowergridCreationReport report = pgService.createModelFromFile("ieee14 Test",
            ieee14File)

        then:
        assert report.wasSuccessful()


    }

//    def "upload psse23 file"(){
//        def file = new File("src/test/resources/IEEE14.raw")
//        when: "called upload"
//        def results = pgService.createModelFromFile(file)
//        then:
//        assert results != null
//        assert pgService.model != null
//        assert pgService.model.buses.size() == 14
//
//    }

    def cleanup(){
        pgService = null
    }

}
