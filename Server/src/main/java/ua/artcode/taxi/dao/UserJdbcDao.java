package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.*;
import ua.artcode.taxi.utils.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by serhii on 25.06.16.
 */
public class UserJdbcDao implements UserDao {

    AddressDao addressDao = new AddressDao();
    CarDao carDao = new CarDao();

    @Override
    public User createUser(User user) {
        try (Connection connection =
                     ConnectionFactory.createConnection();
             Statement statement = connection.createStatement()) {

            connection.setAutoCommit(false);
            Address homeAddress = user.getHomeAddress();
            statement.execute(String.format("INSERT INTO cities (city_name) VALUES ('%s')", homeAddress.getCity()));

            ResultSet resultSet = statement.executeQuery(String.format("SELECT id FROM cities c WHERE c.city_name = '%s' LIMIT 1", homeAddress.getCity()));

            resultSet.next();
            int cityId = resultSet.getInt("id");

            String sqlInsertAddress = String.format("INSERT INTO addresses (city_id, street, num) VALUES (%d,'%s','%s')",
                    cityId, homeAddress.getStreet(), homeAddress.getHouseNum());

            statement.execute(sqlInsertAddress);

            ResultSet resultSet2 = statement.executeQuery("SELECT id FROM addresses s ORDER BY id DESC LIMIT 1;");

            resultSet2.next();
            int addressId = resultSet2.getInt("id");

            String sqlInsert = "INSERT INTO clients(client_name, phone, address_id) " +
                    "VALUES ('" + user.getName() + "', '" + user.getPhone() + "', " + addressId + ");";
            statement.execute(sqlInsert);

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public User updateUser(User newUser) {
        return null;
    }

    @Override
    public User deleteUser(int id) {

        try (Connection connection =
                     ConnectionFactory.createConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM clients c WHERE c.id = ?;")) {

            preparedStatement.setInt((int) 1, id);
            preparedStatement.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User find(String phone) {
        return null;
    }

    @Override
    public List<User> getAllPassenger() {
        List<User> passengers = new ArrayList<>();
        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSetId = statement.executeQuery("SELECT id FROM identifiers WHERE type = 'P' LIMIT 1;");
            int idForPassenger = resultSetId.getInt("id");
            String sqlSelect = String.format("SELECT pass, identifier_id, phone, name, address_id FROM users WHERE identifier_id = %d;", idForPassenger);
            ResultSet resultSet = statement.executeQuery(sqlSelect);
            while (resultSet.next()) {
                //int id = resultSet.getInt("id");
                String pass = resultSet.getString("pass");
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
        return passengers;
    }

    @Override
    public List<User> getAllDrivers() {
        List<User> drivers = new ArrayList<>();
        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSetId = statement.executeQuery("SELECT id FROM identifiers WHERE type = 'D';");
            int idForDriver = resultSetId.getInt("id");
            String sqlSelect = String.format("SELECT pass, identifier_id, phone, name, car FROM users WHERE car_id = %d;", idForDriver);
            ResultSet resultSet = statement.executeQuery(sqlSelect);
            while (resultSet.next()) {
                //int id = resultSet.getInt("id");
                String pass = resultSet.getString("pass");
                UserIdentifier identifier_id = getUserIdentifier(resultSet.getInt("identifier_id"));
                String phone = resultSet.getString("phone");
                String name = resultSet.getString("name");
                Car car = carDao.findById(resultSet.getInt("car"));

                drivers.add(new User(identifier_id, phone, pass, name, car));
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }


    @Override
    public List<Order> getOrdersOfUser(User user) {

        return null;
    }

    public UserIdentifier getUserIdentifier(int identifier_id) {

        try(Connection connection = ConnectionFactory.createConnection();
            Statement statement = connection.createStatement()) {
            ResultSet resultSetId = statement.executeQuery(String.format("SELECT type FROM identifiers WHERE id = %d", identifier_id));

            String type = resultSetId.getString("type");
            UserIdentifier userIdentifier = UserIdentifier.valueOf(type);

            return userIdentifier;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //
        return null;
    }


}
