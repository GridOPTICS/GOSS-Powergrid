package pnnl.goss.powergrid.server.impl

import java.beans.PersistenceDelegate;

import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence;

import pnnl.goss.powergrid.PowergridCreationReport;
import pnnl.goss.powergrid.server.impl.PowergridServiceImpl
import spock.lang.Specification

class PowergridServiceImplSpecs extends Specification {
    PowergridServiceImpl pgService
    EntityManagerFactory factory

    def setup(){
        factory = Persistence.createEntityManagerFactory("mysqlPU")
        pgService = new PowergridServiceImpl()
        pgService.setEntityManagerFactory(factory)
    }

    def "can create powergrid from pti23 IEEE14 bus file"(){
        File ieee14File = new File("src/test/resources/118.raw")
        expect:
        ieee14File.exists()

        when: "Calling the service with an existing file."
        PowergridCreationReport report = pgService.createModelFromFile("ieee14 Test",
            ieee14File)

        then:
        assert report.wasSuccessful()
        // Retrieve all of the powergrids so we can find the one we want.
        def powergrids = pgService.getPowergrids()
        assert powergrids.size() == 1  // There is only one...so grab it.
        def pg = powergrids.get(0)
        def pgModel = pgService.getPowergridModel(pg.mrid)  // now get our model.

        // TODO Added test for other powergrid properties.
        assert pgModel.powergrid.name == pg.name
        assert pgModel.powergrid.mrid == pg.mrid
		

    }


    def cleanup(){
        factory.close()
        factory = null
        pgService = null
    }

}
