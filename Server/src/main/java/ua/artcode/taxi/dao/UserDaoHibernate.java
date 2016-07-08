package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.User;
import ua.artcode.taxi.model.UserIdentifier;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Collection;
import java.util.List;

// CRUD, Create, Read, Update, Delete
public interface UserDaoHibernate {

    User createUser(User user, EntityManagerFactory entityManagerFactory);
    Collection<User> getAllUsers(EntityManagerFactory entityManagerFactory);
    User updateUser(User newUser);
    User deleteUser(int id);

    User findByPhone(String phone, EntityManagerFactory entityManagerFactory);
    User findById(int id);
    List<User> getAllUsersByIdentifier(UserIdentifier identifier, EntityManagerFactory entityManagerFactory);

    List<String> getAllRegisteredPhones(EntityManagerFactory entityManagerFactory);
}