package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.model.UserIdentifier;

import java.util.Collection;
import java.util.List;

// CRUD, Create, Read, Update, Delete
public interface UserDao {

    User createUser(User user);
    Collection<User> getAllUsers();
    User updateUser(User newUser);
    User deleteUser(int id);

    User findByPhone(String phone);
    User findById(int id);
    List<User> getAllUsersByIdentifier(UserIdentifier identifier);

    List<String> getAllRegisteredPhones();
    List<Order> getAllOrdersOfUser(User user);
}