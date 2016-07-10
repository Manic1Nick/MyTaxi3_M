package ua.artcode.taxi.model;

import ua.artcode.taxi.dao.OrderDao;
import ua.artcode.taxi.dao.OrderJdbcDao;
import ua.artcode.taxi.dao.UserDao;
import ua.artcode.taxi.dao.UserJdbcDao;

public class Factory {
    private static Factory instance = null;
    private static OrderDao orderDao = null;
    private static UserDao userDao = null;

    public static synchronized Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserJdbcDao();
        }
        return userDao;
    }

    public OrderDao getOrderDao() {
        if (orderDao == null) {
            orderDao = new OrderJdbcDao(userDao);
        }
        return orderDao;
    }
}