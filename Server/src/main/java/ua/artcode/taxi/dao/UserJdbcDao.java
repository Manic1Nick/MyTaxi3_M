package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.*;
import ua.artcode.taxi.utils.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UserJdbcDao implements UserDao {

    private AddressDao addressDao;
    private CarDao carDao;

    public UserJdbcDao(AddressDao addressDao, CarDao carDao) {
        this.addressDao = addressDao;
        this.carDao = carDao;
    }

    @Override
    public User createUser(User user) {

        //for all users (incl. anonymous)
        User newUser = addBaseUserToJdbc(user.getIdentifier(), user.getPhone(), user.getName());

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement()) {

            connection.setAutoCommit(false);

            String sqlInsert = String.format
                    ("UPDATE users SET pass='%s' WHERE phone='%s';",
                            user.getPass(),
                            newUser.getPhone());
            statement.execute(sqlInsert);

            //for passenger
            if (user.getIdentifier().equals(UserIdentifier.P)) {

                String[] addressArray = user.getHomeAddress().toLine().split(" ");
                long addressId = addressArray.length > 0 ?
                        addressDao.create(user.getHomeAddress()).getId() : -1 ;

                if (addressId > 0) {
                    sqlInsert = String.format
                            ("UPDATE users SET address_id=%d WHERE phone='%s';",
                                    addressId,
                                    newUser.getPhone());
                    statement.execute(sqlInsert);
                }
            }

            //for driver
            else if (user.getIdentifier().equals(UserIdentifier.D)) {
                sqlInsert = String.format
                        ("UPDATE users SET car_id=%d WHERE phone='%s';",
                                carDao.create(user.getCar()).getId(),
                                newUser.getPhone());
                statement.execute(sqlInsert);
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newUser;
    }

    @Override
    public Collection<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            ResultSet resultSet = statement.executeQuery("SELECT id FROM users;");

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

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlUpdate = String.format
                    ("UPDATE users SET identifier_id=%d, phone='%s', pass='%s', name='%s', address_id=%d, car_id=%d WHERE id=%d;",
                            getIdIdentifierFromJdbs(newUser.getIdentifier()),
                            newUser.getPhone(),
                            newUser.getPass(),
                            newUser.getName(),
                            addressDao.update(newUser.getHomeAddress()).getId(),
                            carDao.update(newUser.getCar()).getId(),
                            newUser.getId());
            statement.executeQuery(sqlUpdate);

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newUser;
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
                    ("SELECT id FROM users WHERE phone='%s';", phone);
            ResultSet resultSet = statement.executeQuery(sqlSelect);
            resultSet.next();

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

            String sqlSelect = String.format("SELECT id FROM users WHERE identifier_id=%d;",
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
    public User findById(int id) {

        User user = null;

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format("SELECT * FROM users WHERE id=%d;", id);
            ResultSet resultSet = statement.executeQuery(sqlSelect);
            resultSet.next();

            user = new User(
                    getUserIdentifierByIdFromJdbc(resultSet.getInt("identifier_id")),
                    resultSet.getString("phone"),
                    resultSet.getString("name")
            );

            user.setId(resultSet.getInt("id"));
            user.setPass(resultSet.getString("pass"));
            user.setHomeAddress(addressDao.findById(resultSet.getInt("address_id")));
            user.setCar(carDao.findById(resultSet.getInt("car_id")));

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public List<String> getAllRegisteredPhones() {

        List<String> phones = new ArrayList<>();

        try(Connection connection = ConnectionFactory.createConnection();
            Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            ResultSet resultSet = statement.executeQuery("SELECT phone FROM users;");

            while(resultSet.next()) {
                phones.add(resultSet.getString("phone"));
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return phones;
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
            resultSet.next();
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

        String sqlSelect = String.format("SELECT id FROM identifiers WHERE type='%s';",
                identifier.toString());

        ResultSet resultSet = statement.executeQuery(sqlSelect);
        resultSet.next();

        return resultSet.getInt("id");
    }

    public User addBaseUserToJdbc(UserIdentifier identifier, String phone, String name) {

        User baseUser = new User(identifier, phone, name);

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement()) {

            connection.setAutoCommit(false);

            int identifierId = getIdIdentifierFromJdbs(identifier);

            String sqlInsert = String.format
                    ("INSERT INTO users(identifier_id, phone, name) VALUES (%d, '%s', '%s');",
                            identifierId,
                            baseUser.getPhone(),
                            baseUser.getName());
            statement.execute(sqlInsert);

            //set id for new user
            ResultSet resultSet = statement.executeQuery("SELECT id FROM users s ORDER BY id DESC LIMIT 1;");
            resultSet.next();
            baseUser.setId(resultSet.getInt("id"));

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return baseUser;
    }
}

