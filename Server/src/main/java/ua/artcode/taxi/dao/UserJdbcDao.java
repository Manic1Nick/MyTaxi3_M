package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.*;
import ua.artcode.taxi.utils.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UserJdbcDao implements UserDao {


    @Override
    public User createUser(User user) {

        Address address = user.getHomeAddress();
        Car car = user.getCar();
        UserIdentifier identifier = user.getIdentifier();

        try (Connection connection =
                     ConnectionFactory.createConnection();
             Statement statement = connection.createStatement()) {

            connection.setAutoCommit(false);

            int addressId = insertAddressToJdbcAndGetId(address);
            int carId = insertCarToJdbcAndGetId(car);
            int identifierId = getIdIdentifierFromJdbs(identifier);

            if (addressId >= 0) {
                String sqlInsert = String.format
                        ("INSERT INTO users(identifier_id, phone, name, pass, address_id) VALUES (%d, '%s', '%s','%s', %d)",
                                identifierId,
                                user.getPhone(),
                                user.getName(),
                                user.getPass(),
                                addressId);
                statement.execute(sqlInsert);
            }

            //for driver
            if (carId >= 0) {
                String sqlInsert = String.format
                        ("INSERT INTO users(identifier_id, phone, name, pass, car_id) VALUES (%d, '%s', '%s','%s', %d)",
                                identifierId,
                                user.getPhone(),
                                user.getName(),
                                user.getPass(),
                                carId);
                statement.execute(sqlInsert);
                //todo user.getCar().setId(carId);
            }

            //set id for new user
            user.setId(find(user.getPhone()).getId());

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public Collection<User> getAllUsers() {

        //todo take from gelAllPassengers and getAllDrivers

        return null;
    }

    @Override
    public User updateUser(User newUser) {

        try (Connection connection =
                     ConnectionFactory.createConnection()) {

            connection.setAutoCommit(false);

            deleteUser(newUser.getId());
            createUser(newUser);

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newUser;
    }

    @Override
    public User deleteUser(int id) {

        try(Connection connection =
                    ConnectionFactory.createConnection();
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("DELETE FROM clients c WHERE c.id = ?;")){

            preparedStatement.setInt((int) 1, id);
            preparedStatement.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User find(String phone) {

        User user = null;

        try (Connection connection =
                     ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format
                    ("SELECT id FROM users WHERE phone='%s'", phone);
            statement.execute(sqlSelect);

            ResultSet resultSet = statement.executeQuery(sqlSelect);
            user = findUserById(resultSet.getInt("id"));

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public List<User> getAllPassenger() {

        //todo by Ivan

        return null;
    }

    @Override
    public List<User> getAllDrivers() {

        //todo by Ivan

        return null;
    }

    @Override
    public List<Order> getOrdersOfUser(User user) {

        List<Order> orders = new ArrayList<>();

        try (Connection connection =
                     ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = null;
            if (user.getIdentifier().equals(UserIdentifier.P)) {
                sqlSelect = String.format
                        ("SELECT id, status_id, addressfrom, addressto, passenger_id, driver_id, distance, price, message FROM orders WHERE passenger_id='%s'", user.getId());

            } else if (user.getIdentifier().equals(UserIdentifier.D)) {
                sqlSelect = String.format
                        ("SELECT id, status_id, addressfrom, addressto, passenger_id, driver_id, distance, price, message FROM orders WHERE driver_id='%s'", user.getId());
            }

            ResultSet resultSet = statement.executeQuery(sqlSelect);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");

                sqlSelect = String.format("SELECT type FROM statuses WHERE id='%s';", resultSet.getInt("status_id"));
                ResultSet resultSetId = statement.executeQuery(sqlSelect);
                int typeOrderStatus = resultSetId.getInt("type");

                //OrderStatus status = resultSet.getString("pass");
                UserIdentifier identifier_id = getUserIdentifier(resultSet.getInt("identifier_id"));
                String phone = resultSet.getString("phone");
                String name = resultSet.getString("name");
                Address address_id = addressDao.findById(resultSet.getInt("address_id"));

                passengers.add(new User(identifier_id, phone, pass, name, address_id));
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    //------------------------------------------------------------------------------------
    //additional methods

    public int insertAddressToJdbcAndGetId(Address address) throws SQLException {

        Connection connection =
                ConnectionFactory.createConnection();
        Statement statement = connection.createStatement();

        String sqlInsert = String.format
                ("INSERT INTO addresses (country, city, street, num) VALUES ('%s', '%s','%s','%s')",
                        address.getCountry(),
                        address.getCity(),
                        address.getStreet(),
                        address.getHouseNum());
        statement.execute(sqlInsert);

        ResultSet resultSet = statement.executeQuery
                ("SELECT id FROM addresses s ORDER BY id DESC LIMIT 1;");
        resultSet.next();

        return resultSet.getInt("id");
    }

    public int insertCarToJdbcAndGetId(Car car) throws SQLException {
        Connection connection =
                ConnectionFactory.createConnection();
        Statement statement = connection.createStatement();

        String sqlInsert = String.format
                ("INSERT INTO cars (type, model, number) VALUES ('%s', '%s','%s')",
                        car.getType(),
                        car.getModel(),
                        car.getNumber());
        statement.execute(sqlInsert);

        ResultSet resultSet = statement.executeQuery
                ("SELECT id FROM cars s ORDER BY id DESC LIMIT 1;");
        resultSet.next();

        return resultSet.getInt("id");
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

    public User findUserById(int id) throws SQLException {

        User user = null;

        Connection connection =
                ConnectionFactory.createConnection();
        Statement statement = connection.createStatement();

        //todo take create user from getAllPassengers and getAllDrivers

        /*String sqlSelect = String.format
                ("SELECT identifier, phone, pass, name, address, car FROM users WHERE id VALUE '%d'", id);
        statement.execute(sqlSelect);

        ResultSet resultSet = statement.executeQuery(sqlSelect);
        resultSet.next();*/

        return user;
    }
}

