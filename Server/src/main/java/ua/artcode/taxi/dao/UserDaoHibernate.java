package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.User;
import ua.artcode.taxi.model.UserIdentifier;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

// CRUD, Create, Read, Update, Delete
public interface UserDaoHibernate {

    User createUser(User user, EntityManager manager);
    Collection<User> getAllUsers(EntityManager manager);
    User updateUser(User newUser);
    User deleteUser(int id);

    User findByPhone(String phone, EntityManager manager);
    User findById(int id);
    List<User> getAllUsersByIdentifier(UserIdentifier identifier, EntityManager manager);

    List<String> getAllRegisteredPhones(EntityManager manager);
}