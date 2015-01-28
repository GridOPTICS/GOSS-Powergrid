package pnnl.goss.powergrid.server.impl

import java.io.File;

import pnnl.goss.powergrid.entities.PowergridModelEntity
import pnnl.goss.powergrid.parsers.ResultLog
import pnnl.goss.powergrid.parsers.PsseParser;
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

class PowergridBuilderSpecs extends Specification {

    def "can create powergridmodelentity from IEEE14 parsed model"() {
        def rawFile = new File("src/test/resources/IEEE14.raw")
        Psse23TestParser testParser = new Psse23TestParser(rawFile)

        assert rawFile.exists()
        assert testParser.parser != null
        PsseParser parser = testParser.parser
        assert parser.modelValid

        PowergridBuilder builder = new PowergridBuilder()
        when: "building a powergrid entity"
        PowergridModelEntity powergridEntity = builder.createFromParser(parser)
        then:
        assert powergridEntity.busEntities.size()== 14
    }

//    def "persists to mysql"() {
//
//        Powerg
//        PowergridModelEntity persist = new PowergridModelEntity()
//        persist.setCharacteristics("Characteristic")
//        persist.setMrid("mrid what?")
//        //persist.setPowergridName("My name is?")
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cassandra_pu");
//        EntityManager em = emf.createEntityManager()
//
//        assert em != null
//
//        when: "saving using persistence."
//        em.persist(persist)
//        then:
//        em.close()
//        emf.close()
//    }
//
//
//    def "persistence saves to cassandra"() {
//        PowergridModelEntity persist = new PowergridModelEntity()
//        persist.setCharacteristics("Characteristic")
//        persist.setMrid("mrid what?")
//        //persist.setPowergridName("My name is?")
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysqlPU");
//        EntityManager em = emf.createEntityManager()
//
//        assert em != null
//
//        when: "saving using persistence."
//        em.persist(persist)
//        then:
//        em.close()
//        emf.close()
//
//
//    }
//
//    def setup(){
//        tempDir = tempDirContainer.createTempDir()
//        parser = new PsseParser()
//    }
//
//    def cleanup(){
//        tempDir.deleteDir()
//        parser = null
//    }

}
