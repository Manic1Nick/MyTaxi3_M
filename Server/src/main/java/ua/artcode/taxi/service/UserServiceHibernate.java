package ua.artcode.taxi.service;

import ua.artcode.taxi.exception.*;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;

import javax.persistence.EntityManager;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * Created by serhii on 23.04.16.
 */
public interface UserServiceHibernate {

    //register
    User registerPassenger(Map<String, String> map, EntityManager manager) throws RegisterException;
    User registerDriver(Map<String, String> map, EntityManager manager) throws RegisterException;

    //login (return accessToken)
    String login(String phone, String pass, EntityManager manager) throws Exception;

    //actions for passenger
    Order makeOrder(String accessToken, String lineFrom, String lineTo, String message, EntityManager manager) throws OrderMakeException,
                                                UserNotFoundException, InputDataWrongException, UnknownHostException;
    Order makeOrderAnonymous(String phone, String name, String from, String to, String message, EntityManager manager) throws
                                            OrderMakeException, InputDataWrongException, UnknownHostException;
    Map<String, Object> calculateOrder(String lineFrom, String lineTo, EntityManager manager) throws
                                            InputDataWrongException, UnknownHostException;
    Order getOrderInfo(long orderId) throws OrderNotFoundException;
    Order getLastOrderInfo(String accessToken) throws UserNotFoundException, OrderNotFoundException;
    Order cancelOrder(long orderId) throws OrderNotFoundException;
    Order closeOrder(String accessToken, long orderId) throws OrderNotFoundException,
                                    WrongStatusOrderException, DriverOrderActionException;

    //actions for driver
    Order takeOrder(String accessToken, long orderId)
            throws OrderNotFoundException, WrongStatusOrderException, DriverOrderActionException;
    Map<Integer, Order> getMapDistancesToDriver(String orderStatus, String lineAddressDriver)
                                                                            throws InputDataWrongException;

    //actions for all
    User getUser(String accessToken);
    List<Order> getAllOrdersUser(String accessToken);
    User updateUser(Map<String, String> map, String accessToken, EntityManager manager) throws RegisterException;
    User deleteUser(String accessToken);
}
