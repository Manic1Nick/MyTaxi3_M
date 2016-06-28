package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.*;
import ua.artcode.taxi.utils.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UserJdbcDao implements UserDao {

    private AddressDao addressDao;
    private OrderJdbcDao orderDao;
    private CarDao carDao;

    @Override
    public User createUser(User user) {

        UserIdentifier identifier = user.getIdentifier();

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement()) {

            connection.setAutoCommit(false);

            int identifierId = getIdIdentifierFromJdbs(identifier);

            String sqlInsert = String.format
                    ("INSERT INTO users(identifier_id, phone, name) VALUES (%d, '%s', '%s')",
                            identifierId,
                            user.getPhone(),
                            user.getName());
            statement.execute(sqlInsert);

            //for passenger
            if (identifier.equals(UserIdentifier.P)) {
                sqlInsert = String.format
                        ("INSERT INTO users(pass, address_id) WHERE phone='%s' VALUES ('%s', %d)",
                                user.getPhone(),
                                user.getPass(),
                                addressDao.create(user.getHomeAddress()).getId());
                statement.execute(sqlInsert);
            }

            //for driver
            else if (identifier.equals(UserIdentifier.D)) {
                sqlInsert = String.format
                        ("INSERT INTO users(pass, car_id) WHERE phone='%s' VALUES ('%s', %d)",
                                user.getPhone(),
                                user.getPass(),
                                carDao.create(user.getCar()).getId());
                statement.execute(sqlInsert);
            }

            //set id for new user
            ResultSet resultSet = statement.executeQuery("SELECT id FROM users s ORDER BY id DESC LIMIT 1;");
            resultSet.next();
            user.setId(resultSet.getInt("id"));

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public Collection<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            ResultSet resultSet = statement.executeQuery("SELECT id FROM users");

            while (resultSet.next()) {
                users.add(findById(resultSet.getInt("id")));
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public User updateUser(User newUser) {

        deleteUser(newUser.getId());

        return createUser(newUser);
    }

    @Override
    public User deleteUser(int id) {

        User user = findById(id);

        try(Connection connection =
                    ConnectionFactory.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("DELETE FROM clients c WHERE c.id = ?;")){

            preparedStatement.setInt((int) 1, id);
            preparedStatement.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User findByPhone(String phone) {

        User user = null;

        try (Connection connection =
                     ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format
                    ("SELECT id FROM users WHERE phone='%s'", phone);
            statement.execute(sqlSelect);

            ResultSet resultSet = statement.executeQuery(sqlSelect);
            user = findById(resultSet.getInt("id"));

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public List<User> getAllUsersByIdentifier(UserIdentifier identifier) {

        List<User> users = new ArrayList<>();

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format("SELECT id FROM users WHERE identifier_id=%d",
                    getIdIdentifierFromJdbs(identifier));
            ResultSet resultSet = statement.executeQuery(sqlSelect);

            while (resultSet.next()) {
                users.add(findById(resultSet.getInt("id")));
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public List<Order> getOrdersOfUser(User user) {

        List<Order> orders = new ArrayList<>();

        try (Connection connection =
                     ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format
                    ("SELECT id FROM orders WHERE passenger_id='%s' OR driver_id='%s'",
                            user.getId(), user.getId());
            ResultSet resultSet = statement.executeQuery(sqlSelect);

            while (resultSet.next()) {
                orders.add(orderDao.findById(resultSet.getLong("id")));
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public User findById(int id) {

        User user = null;

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format("SELECT * FROM users WHERE id=%d", id);
            ResultSet resultSet = statement.executeQuery(sqlSelect);

            user.setIdentifier(getUserIdentifierByIdFromJdbc(resultSet.getInt("identifier_id")));
            user.setPhone(resultSet.getString("phone"));
            user.setPass(resultSet.getString("pass"));
            user.setName(resultSet.getString("name"));
            user.setHomeAddress(addressDao.findById(resultSet.getInt("address_id")));
            user.setCar(carDao.findById(resultSet.getInt("car_id")));

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }


    //------------------------------------------------------------------------------------
    //additional methods

    public UserIdentifier getUserIdentifierByIdFromJdbc(int id) {

        UserIdentifier identifier = null;

        try(Connection connection = ConnectionFactory.createConnection();
            Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format("SELECT type FROM identifiers WHERE id=%d;", id);
            ResultSet resultSet = statement.executeQuery(sqlSelect);
            String typeUserIdentifier = resultSet.getString("type");

            if (typeUserIdentifier.equals("P")) {
                identifier = UserIdentifier.P;
            } else if (typeUserIdentifier.equals("D")) {
                identifier = UserIdentifier.D;
            } else if (typeUserIdentifier.equals("A")) {
                identifier = UserIdentifier.A;
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return identifier;
    }


    public int getIdIdentifierFromJdbs(UserIdentifier identifier) throws SQLException {
        Connection connection =
                ConnectionFactory.createConnection();
        Statement statement = connection.createStatement();

        String sqlSelect = String.format("SELECT id FROM identifiers WHERE identifier='%s'",
                identifier);

        ResultSet resultSet = statement.executeQuery(sqlSelect);

        return resultSet.getInt("id");
    }
}

