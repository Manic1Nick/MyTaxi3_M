package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;
import ua.artcode.taxi.model.User;

import java.util.Collection;
import java.util.List;

public interface OrderDao {

    Order create(User user, Order order);
    Collection<Order> getAll();
    Order update(Order newOrder);
    Order delete(long id);

    Order findById(long id);
    List<Order> getOrdersByStatus(OrderStatus status);
    Order addToDriver(User user, Order order);

    OrderStatus getOrderStatusById(long id);
}
