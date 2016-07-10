package ua.artcode.taxi.test;

import ua.artcode.taxi.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactoryTest {

    public static void main(String[] args) {

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("myunit");

        EntityManager manager = entityManagerFactory.createEntityManager();

        User passenger1 = new User(UserIdentifier.P,
                "1234", "test", "Vasya", new Address("Ukraine", "Kiev", "Khreschatik", "5"));
        User passenger2 = new User(UserIdentifier.P,
                "1111", "test1", "Ivan", new Address("Ukraine", "Kiev", "Zhukova", "51"));

        User driver1 = new User(UserIdentifier.D,
                "5678", "test", "Petya", new Car("sedan", "skoda rapid", "2233"));
        User driver2 = new User(UserIdentifier.D,
                "2222", "test1", "Dima", new Car("pickup", "mitsubishi l200", "2346"));


        //test current orders for driver
        Order order1 = new Order (new Address("Ukraine", "Kiev", "Zhukova", "51"),
                new Address("Ukraine", "Kiev", "Khreschatik", "5"), passenger1, 10, 100, "I have a dog!:)");
        Order order2 = new Order(new Address("Ukraine", "Kiev", "Khreschatik", "11"),
                new Address("Ukraine", "Kiev", "Khreschatik", "5"), passenger2, 1, 10, "I have a cat!:(");
        Order order3 = new Order (new Address("Ukraine", "Kiev", "Starokievskaya", "1"),
                new Address("Ukraine", "Kiev", "Khreschatik", "5"), passenger1, 20, 200, "");
        Order order4 = new Order(new Address("Ukraine", "Kiev", "Perova", "10"),
                new Address("Ukraine", "Kiev", "Khreschatik", "5"), passenger2, 15, 150, "");
        Order order5 = new Order (new Address("Ukraine", "Kiev", "Shevchenka", "30"),
                new Address("Ukraine", "Kiev", "Khreschatik", "5"), passenger2, 2, 20, "");
        Order order6 = new Order(new Address("Ukraine", "Kiev", "Liskovskaya", "33"),
                new Address("Ukraine", "Kiev", "Khreschatik", "5"), passenger1, 30, 250, "");



        manager.getTransaction().begin();

        manager.persist(passenger1);
        manager.persist(passenger2);
        manager.persist(driver1);
        manager.persist(driver2);
        manager.persist(order1);
        manager.persist(order2);
        manager.persist(order3);
        manager.persist(order4);
        manager.persist(order5);
        manager.persist(order6);


        manager.getTransaction().commit();

    }
}
