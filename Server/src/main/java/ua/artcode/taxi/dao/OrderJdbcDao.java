package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.utils.ConnectionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderJdbcDao implements OrderDao {

    private EntityManager manager = ConnectionFactory.createEntityManager();

    private UserDao userDao;

    public OrderJdbcDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Order create(User user, Order order) {

        order.setOrderStatus(OrderStatus.NEW);
        order.setPassenger(user);

        manager.getTransaction().begin();
        manager.persist(order);
        manager.getTransaction().commit();

        return order;
    }

    @Override
    public Collection<Order> getAllOrders() {

        manager.getTransaction().begin();
        Query query = manager.createNamedQuery("getAllOrders");
        Collection<Order> orders = query.getResultList();
        manager.getTransaction().commit();

        return orders;
    }

    @Override
    public Order update(Order newOrder) {

        manager.getTransaction().begin();
        manager.persist(newOrder);
        manager.getTransaction().commit();

        return newOrder;
    }

    @Override
    public Order delete(long id) {

        Order delOrder = findById(id);

        manager.getTransaction().begin();
        manager.remove(delOrder);
        manager.getTransaction().commit();

        return delOrder;
    }

    @Override
    public Order findById(long id) {

        manager.getTransaction().begin();
        Order order = manager.find(Order.class, id);
        manager.getTransaction().commit();

        return order;
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {

        List<Order> ordersRes = new ArrayList<>();

        Collection<Order> orders = getAllOrders();
        for (Order order : orders) {
            if(order.getOrderStatus().equals(status)) {
                ordersRes.add(order);
            }
        }
        return ordersRes;
    }

    @Override
    public List<Order> getOrdersOfUser(User user) {

        return userDao.getAllOrdersOfUser(user);
    }

    @Override
    public Order addToDriver(User user, Order order) {

        order.setDriver(user);

        manager.getTransaction().begin();
        manager.persist(order);
        manager.getTransaction().commit();

        return order;
    }

    @Override
    public OrderStatus getOrderStatusById(long id) {

        OrderStatus status = null;

        Collection<Order> orders = getAllOrders();
        for (Order order : orders) {
            if(order.getId() == id) {
                status = order.getOrderStatus();
            }
        }
        return status;
    }
}
