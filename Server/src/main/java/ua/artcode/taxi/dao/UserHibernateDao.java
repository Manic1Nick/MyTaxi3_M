package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.User;
import ua.artcode.taxi.model.UserIdentifier;
import ua.artcode.taxi.utils.ConnectionFactory;

import javax.persistence.EntityManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UserHibernateDao implements UserDaoHibernate {

    private AddressDao addressDao;
    private CarDao carDao;

    public UserHibernateDao(AddressDao addressDao, CarDao carDao) {
        this.addressDao = addressDao;
        this.carDao = carDao;
    }

    @Override
    public User createUser(User user, EntityManager manager) {

        //for all users (incl. anonymous)
        manager.getTransaction().begin();
        manager.persist(user);
        manager.getTransaction().commit();

        return user;
    }

    @Override
    public Collection<User> getAllUsers(EntityManager manager) {

        List<User> users = new ArrayList<>();
        manager.getTransaction().begin();
        users =  manager.createQuery("SELECT u FROM User u", User.class)
                .getResultList();

        return users;
    }

    @Override
    public User updateUser(User newUser) {

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlUpdate = String.format
                    ("UPDATE users SET identifier_id=%d, phone='%s', pass='%s', name='%s', address_id=%d, car_id=%d WHERE id=%d;",
                            newUser.getIdentifier(),
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
    public User findByPhone(String phone, EntityManager manager) {

        User user = null;

        manager.getTransaction().begin();
        user =  manager.createQuery("SELECT u FROM User u WHERE u.phone = :uPhone", User.class)
                .setParameter("uPhone", phone).getSingleResult();

        return user;
    }

    @Override
    public List<User> getAllUsersByIdentifier(UserIdentifier identifier, EntityManager manager) {

        List<User> users = new ArrayList<>();
        manager.getTransaction().begin();
        users =  manager.createQuery("SELECT u FROM User u WHERE u.identifier = :uIdentifier", User.class)
                .setParameter("uIdentifier", identifier.toString()).getResultList();

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
    public List<String> getAllRegisteredPhones(EntityManager manager) {

        List<String> phones = new ArrayList<>();

        manager.getTransaction().begin();
        phones =  manager.createQuery("SELECT phone FROM User").getResultList();

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



    public User addBaseUserToJdbc(UserIdentifier identifier, String phone, String name, EntityManager manager) {

        User baseUser = new User(identifier, phone, name);

        manager.getTransaction().begin();
        manager.persist(baseUser);
        manager.getTransaction().commit();

        return baseUser;
    }
}

