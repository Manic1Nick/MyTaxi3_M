package ua.artcode.taxi.utils;

import ua.artcode.taxi.dao.*;
import ua.artcode.taxi.model.*;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.service.UserServiceImpl;
import ua.artcode.taxi.service.ValidatorImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by serhii on 02.07.16.
 */
public class BeansFactory {

    public static Map<String, Object> objectMap = new ConcurrentHashMap<>();

    // todo singleton with multithreading
    public synchronized static UserService createUserService() {
        if(objectMap.containsKey("userService")){
            return (UserService) objectMap.get("userService");
        } else {
            AppDB appDB = new AppDB();
            UserDao userDao = new UserDaoInnerDbImpl(appDB);
            OrderDao orderDao = new OrderDaoInnerDbImpl(appDB);
            ValidatorImpl validator = new ValidatorImpl(appDB);


            UserService userService = new UserServiceImpl(userDao, orderDao, validator);

            initData(userDao, orderDao);

            objectMap.put("userService", userService);
            return userService;
        }

    }

    private static void initData(UserDao userDao, OrderDao orderDao) {
        User passenger1 = new User(UserIdentifier.P,
                "1234", "test", "Vasya", new Address("Ukraine", "Kiev", "Khreschatik", "5"));
        User passenger2 = new User(UserIdentifier.P,
                "1111", "test1", "Ivan", new Address("Ukraine", "Kiev", "Zhukova", "51"));
        userDao.createUser(passenger1);
        userDao.createUser(passenger2);

        User driver1 = new User(UserIdentifier.D,
                "5678", "test", "Petya", new Car("sedan", "skoda rapid", "2233"));
        User driver2 = new User(UserIdentifier.D,
                "2222", "test1", "Dima", new Car("pickup", "mitsubishi l200", "2346"));
        userDao.createUser(driver1);
        userDao.createUser(driver2);

        //test current orders for driver
        Order order1 = new Order(new Address("Ukraine", "Kiev", "Zhukova", "51"),
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

        orderDao.create(passenger1, order1);
        orderDao.create(passenger2, order2);
        orderDao.create(passenger1, order3);
        orderDao.create(passenger2, order4);
        orderDao.create(passenger2, order5);
        orderDao.create(passenger1, order6);
    }
}
