package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

public class OrderJdbcDao implements OrderDao {

    private UserDao userDao = new UserJdbcDao();
    private AddressDao addressDao = new AddressDao();

    @Override
    public Order create(User user, Order order) {
        return null;
    }

    @Override
    public Collection<Order> getAll() {
        return null;
    }

    @Override
    public Order update(Order newOrder) {
        return null;
    }

    @Override
    public Order delete(long id) {
        return null;
    }

    @Override
    public Order find(long id) {

        Order order = new Order();

        try (Connection connection =
                ConnectionFactory.createConnection();
            Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format
                    ("SELECT status_id, addressfrom_id, addressto_id, passenger_id, driver_id, distance, price, message FROM orders WHERE id=%d", id);
            ResultSet resultSet = statement.executeQuery(sqlSelect);

            //enum :)
            String sqlSelect2 = String.format("SELECT type FROM statuses WHERE id='%s';", resultSet.getInt("status_id"));
            ResultSet resultSetId = statement.executeQuery(sqlSelect2);
            String typeOrderStatus = resultSetId.getString("type");

            if (typeOrderStatus.equals("NEW")) {
                order.setOrderStatus(OrderStatus.NEW);
            } else if (typeOrderStatus.equals("IN_PROGRESS")) {
                order.setOrderStatus(OrderStatus.IN_PROGRESS);
            } else if (typeOrderStatus.equals("CANCELLED")) {
                order.setOrderStatus(OrderStatus.CANCELLED);
            } else if (typeOrderStatus.equals("DONE")) {
                order.setOrderStatus(OrderStatus.DONE);
            }

            order.setFrom(addressDao.findById(resultSet.getInt("addressfrom_id")));
            order.setTo(addressDao.findById(resultSet.getInt("addressto_id")));
            order.setPassenger(userDao.findById(resultSet.getInt("passenger_id")));
            order.setDriver(userDao.findById(resultSet.getInt("driver_id")));
            order.setDistance(resultSet.getInt("distance"));
            order.setPrice(resultSet.getInt("price"));
            order.setMessage(resultSet.getString("message"));

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return null;
    }

    @Override
    public Order addToDriver(User user, Order order) {
        return null;
    }
}
