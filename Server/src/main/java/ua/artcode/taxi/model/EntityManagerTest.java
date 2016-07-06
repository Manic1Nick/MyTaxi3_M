package ua.artcode.taxi.model;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by Iurii on 06.07.2016.
 */
public class EntityManagerTest {
    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myunit");


    }
}
