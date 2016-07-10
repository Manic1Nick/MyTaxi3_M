package ua.artcode.taxi.utils;

import ua.artcode.taxi.dao.*;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.service.UserServiceJdbcImpl;
import ua.artcode.taxi.service.ValidatorJdbcImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeansFactory {

    public static Map<String, Object> objectMap = new ConcurrentHashMap<>();

    //todo singleton with multithreading
    public synchronized static UserService createUserService() {

        if (objectMap.containsKey("userService")) {
            return (UserService) objectMap.get("userService");

        } else {
            UserDao userDao = new UserJdbcDao();
            OrderDao orderDao = new OrderJdbcDao(userDao);
            ValidatorJdbcImpl validator = new ValidatorJdbcImpl(userDao);

            return new UserServiceJdbcImpl(userDao, orderDao, validator);
        }


    }
}
