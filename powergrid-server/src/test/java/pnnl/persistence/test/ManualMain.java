package pnnl.persistence.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import pnnl.goss.powergrid.entities.PowergridModelEntity;

public class ManualMain {

    public static void main(String[] args) {
        PowergridModelEntity powergridEntity = new PowergridModelEntity();
        //powergridEntity.setCharacteristics("Characteristic");
        powergridEntity.setMrid("mrid what?");
        powergridEntity.setPowergridName("My name is?");
        //EntityManagerFactory emf = Persistence.createEntityManagerFactory("cassandra_pu");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysqlPU");

        EntityManager em = emf.createEntityManager();

        em.persist(powergridEntity);
        em.close();
        emf.close();
    }

}
