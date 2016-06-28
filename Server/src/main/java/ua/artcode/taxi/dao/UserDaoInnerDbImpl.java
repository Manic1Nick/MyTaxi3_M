package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.model.UserIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDaoInnerDbImpl implements UserDao {

    private AppDB appDB;

    public UserDaoInnerDbImpl(AppDB appDB) {
        this.appDB = appDB;
    }

    @Override
    public User createUser(User user) {
        return appDB.addUser(user);
    }

    @Override
    public Collection<User> getAllUsers() {
        return appDB.getUsers().keySet();
    }

    @Override
    public User updateUser(User newUser) {

        User result = null;

        for (User user : appDB.getUsers().keySet()) {

            if (user.getId() == newUser.getId()) {

                user.setPhone(newUser.getPhone());
                user.setName(newUser.getName());
                user.setPass(newUser.getPass());
                user.setHomeAddress(newUser.getHomeAddress());
                user.setCar(newUser.getCar());

                result = user;
            }
        }
        return result;
    }

    @Override
    public User deleteUser(int id) {

        User result = null;

        for (User user : appDB.getUsers().keySet()) {
            if (user.getId() == id) {
                result = user;
            }
        }
        appDB.getUsers().remove(result);

        return result;
    }

    @Override
    public User findByPhone(String phone) {
        return appDB.findUser(phone);
    }

    @Override
    public List<User> getAllPassenger() {

        List<User> passengers = new ArrayList<>();

        for (User user : appDB.getUsers().keySet()) {
            if (user.getIdentifier().equals(UserIdentifier.P)) {
                passengers.add(user);
            }
        }
        return passengers;
    }

    @Override
    public List<User> getAllDrivers() {

        List<User> drivers = new ArrayList<>();

        for (User user : appDB.getUsers().keySet()) {
            if (user.getIdentifier().equals(UserIdentifier.D)) {
                drivers.add(user);
            }
        }
        return drivers;
    }

    @Override
    public List<Order> getOrdersOfUser(User user) {
        return appDB.getUsers().get(user);
    }

    @Override
    public User findById(int id) {
        return null;
    }
}
