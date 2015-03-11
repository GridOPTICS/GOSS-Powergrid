package pnnl.goss.powergrid.server.impl

import java.io.File;

import pnnl.goss.powergrid.entities.BranchEntity
import pnnl.goss.powergrid.entities.PowergridModelEntity
import pnnl.goss.powergrid.parsers.ResultLog
import pnnl.goss.powergrid.parsers.PsseParser;
import spock.lang.Specification

import javax.enterprise.inject.Model;
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
        ResultLog log = new ResultLog()
        when: "building a powergrid entity"
        PowergridModelEntity powergridEntity = builder.createFromParser(parser, log, [:])
        then:
        assert powergridEntity.busEntities.size()== 14
        assert powergridEntity.branchEntities.size() == 20
        assert powergridEntity.generatorEntities.size() == 5
        assert powergridEntity.branchEntities.size() == 20
        BranchEntity branch1 = powergridEntity.branchEntities[0]
        assert branch1.r == 0.01938
        assert branch1.x == 0.05917
        assert branch1.b == 0.05280
        assert branch1.status == 1
        assert branch1.ckt == 'BL'

        assert powergridEntity.transformerEntities.size() == 0
        assert powergridEntity.areaEntities.size() == 1
        assert powergridEntity.zoneEntities.size() == 1
        assert powergridEntity.ownerEntities.size() == 1
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
