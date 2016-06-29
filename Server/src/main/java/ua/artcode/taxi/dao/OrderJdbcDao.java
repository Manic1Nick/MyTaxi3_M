package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.OrderStatus;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.utils.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderJdbcDao implements OrderDao {

    private UserDao userDao;
    private AddressDao addressDao;

    public OrderJdbcDao(UserDao userDao, AddressDao addressDao) {
        this.userDao = userDao;
        this.addressDao = addressDao;
    }

    @Override
    public Order create(User user, Order order) {

        try(Connection connection = ConnectionFactory.createConnection();
            Statement statement = connection.createStatement();){

            connection.setAutoCommit(false);

            ResultSet resultSet = statement.executeQuery("SELECT id FROM statuses WHERE status='NEW';");
            resultSet.next();

            String sqlInsert = String.format
                    ("INSERT INTO orders(status_id, addressfrom_id, addressto_id, passenger_id, distance, price, message) VALUES (%d, %d, %d, %d, %d, %d, '%s');",
                            resultSet.getInt("id"),
                            addressDao.create(order.getFrom()).getId(),
                            addressDao.create(order.getTo()).getId(),
                            user.getId(),
                            order.getDistance(),
                            order.getPrice(),
                            order.getMessage());

            System.out.println(sqlInsert);

            statement.execute(sqlInsert);

            //set id for new order
            resultSet = statement.executeQuery("SELECT id FROM orders s ORDER BY id DESC LIMIT 1;");
            resultSet.next();
            order.setId(resultSet.getLong("id"));
            order.setOrderStatus(OrderStatus.NEW);
            order.setPassenger(user);

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

    @Override
    public Collection<Order> getAll() {

        Collection<Order> orders = new ArrayList<>();

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            ResultSet resultSet = statement.executeQuery("SELECT id FROM orders;");

            while (resultSet.next()) {
                orders.add(findById(resultSet.getLong("id")));
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public Order update(Order newOrder) {

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlUpdate = String.format
                    ("UPDATE orders SET addressfrom_id=%d, addressto_id=%d, passenger_id=%d, driver_id=%d, distance=%d, price=%d, message='%s' WHERE id=%d;",
                            addressDao.update(newOrder.getFrom()).getId(),
                            addressDao.update(newOrder.getTo()).getId(),
                            userDao.updateUser(newOrder.getPassenger()).getId(),
                            userDao.updateUser(newOrder.getDriver()).getId(),
                            newOrder.getDistance(),
                            newOrder.getPrice(),
                            newOrder.getMessage(),
                            newOrder.getId());
            statement.executeQuery(sqlUpdate);

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newOrder;
    }

    @Override
    public Order delete(long id) {

        Order order = findById(id);

        try(Connection connection =
                    ConnectionFactory.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("DELETE FROM orders c WHERE c.id = ?;")){

            preparedStatement.setLong((int) 1, id);
            preparedStatement.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

    @Override
    public Order findById(long id) {

        Order order = new Order();

        try (Connection connection = ConnectionFactory.createConnection();
            Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format("SELECT * FROM orders WHERE id=%d;", id);
            ResultSet resultSet = statement.executeQuery(sqlSelect);
            resultSet.next();

            order.setId(resultSet.getLong("id"));
            order.setOrderStatus(getOrderStatusById(id));
            order.setFrom(addressDao.findById(resultSet.getInt("addressfrom_id")));
            order.setTo(addressDao.findById(resultSet.getInt("addressto_id")));
            order.setPassenger(userDao.findById(resultSet.getInt("passenger_id")));

            if (resultSet.getInt("driver_id") > 0) {
                order.setDriver(userDao.findById(resultSet.getInt("driver_id")));
            }

            order.setDistance(resultSet.getInt("distance"));
            order.setPrice(resultSet.getInt("price"));
            order.setMessage(resultSet.getString("message"));

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {

        List<Order> orders = new ArrayList<>();

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelectStatus = String.format
                    ("SELECT id FROM statuses WHERE status='%s';" , status.toString());
            ResultSet resultSet = statement.executeQuery(sqlSelectStatus);
            resultSet.next();

            String sqlSelect = String.format
                    ("SELECT * FROM orders WHERE status_id=%d;", resultSet.getInt("id"));
            resultSet = statement.executeQuery(sqlSelect);

            while (resultSet.next()) {
                orders.add(findById(resultSet.getLong("id")));
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public List<Order> getOrdersOfUser(User user) {

        List<Order> orders = new ArrayList<>();

        try (Connection connection =
                     ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format
                    ("SELECT id FROM orders WHERE passenger_id='%s' OR driver_id='%s';",
                            user.getId(), user.getId());
            ResultSet resultSet = statement.executeQuery(sqlSelect);

            while (resultSet.next()) {
                orders.add(findById(resultSet.getLong("id")));
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public Order addToDriver(User user, Order order) {

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlInsert = String.format
                    ("INSERT INTO orders(driver_id) VALUES (%d);", user.getId());
            statement.execute(sqlInsert);

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

    @Override
    public OrderStatus getOrderStatusById(long id) {

        OrderStatus status = null;

        try(Connection connection = ConnectionFactory.createConnection();

            Statement statement = connection.createStatement();){
            connection.setAutoCommit(false);

            String sqlSelect = String.format("SELECT status_id FROM orders WHERE id=%d;",
                    (int) id);
            ResultSet resultSet = statement.executeQuery(sqlSelect);
            resultSet.next();

            String sqlSelect2 = String.format("SELECT status FROM statuses WHERE id=%d;",
                    resultSet.getInt("status_id"));
            ResultSet resultSetId = statement.executeQuery(sqlSelect2);
            resultSetId.next();
            String typeOrderStatus = resultSetId.getString("status");

            if (typeOrderStatus.equals("NEW")) {
                status = OrderStatus.NEW;
            } else if (typeOrderStatus.equals("IN_PROGRESS")) {
                status = OrderStatus.IN_PROGRESS;
            } else if (typeOrderStatus.equals("CANCELLED")) {
                status = OrderStatus.CANCELLED;
            } else if (typeOrderStatus.equals("DONE")) {
                status = OrderStatus.DONE;
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }
}
