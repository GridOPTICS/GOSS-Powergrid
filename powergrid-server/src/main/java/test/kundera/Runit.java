package test.kundera;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Runit {

    public static void main(String[] args) {
        SaveObject user = new SaveObject();
        user.setUserId("0001");
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setCity("London");

        //EntityManagerFactory emf = Persistence.createEntityManagerFactory("cassandra_pu");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysqlPU");

        EntityManager em = emf.createEntityManager();

        em.persist(user);
        em.close();
        emf.close();

    }

}
