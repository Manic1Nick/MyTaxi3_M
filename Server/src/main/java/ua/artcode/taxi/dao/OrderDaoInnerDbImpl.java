package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;
import ua.artcode.taxi.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderDaoInnerDbImpl implements OrderDao {

    private AppDB appDB;

    public OrderDaoInnerDbImpl(AppDB appDB) {
        this.appDB = appDB;
    }

    @Override
    public Order create(User user, Order order) {
        order.setOrderStatus(OrderStatus.NEW);
        return appDB.addOrder(user, order);
    }

    @Override
    public Collection<Order> getAll() {
        return appDB.getOrders();
    }

    @Override
    public Order update(Order newOrder) {

        Order result = null;

        for (Order order : appDB.getOrders()) {

            if (order.getId() == newOrder.getId()) {

                order.setOrderStatus(newOrder.getOrderStatus());

                result = order;
            }
        }
        return result;
    }

    @Override
    public Order delete(long orderId) {
        return null;
    }

    @Override
    public Order findById(long id) {

        return appDB.findOrder(id);
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {

        Collection<Order> allOrders = appDB.getOrders();
        List<Order> orders = new ArrayList<>();

        for (Order order : allOrders) {
            if (order.getOrderStatus().equals(status)){
                orders.add(order);
            }
        }

        return orders;
    }

    @Override
    public Order addToDriver(User user, Order order) {
        return appDB.addOrderToDriver(user, order);
    }

    @Override
    public OrderStatus getOrderStatusById(long id) {
        return null;
    }
}
