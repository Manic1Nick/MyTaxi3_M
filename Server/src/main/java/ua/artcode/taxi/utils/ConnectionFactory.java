package ua.artcode.taxi.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class ConnectionFactory {

    private static Map<Long,EntityManager> managers = new HashMap<>();
    private static EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("myunit");


    public static EntityManager createEntityManager() {
        long id = Thread.currentThread().getId();

        if(!managers.containsKey(id)){
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            managers.put(id, entityManager);
            return entityManager;

        } else {
            return managers.get(id);
        }
    }

    public static void close(){
        long id = Thread.currentThread().getId();
        managers.remove(id);
    }
}
