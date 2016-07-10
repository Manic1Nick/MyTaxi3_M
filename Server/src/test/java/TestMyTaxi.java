import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ua.artcode.taxi.dao.OrderJdbcDao;
import ua.artcode.taxi.dao.UserJdbcDao;
import ua.artcode.taxi.model.*;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.service.UserServiceJdbcImpl;
import ua.artcode.taxi.service.ValidatorJdbcImpl;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPI;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPIImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMyTaxi {

    private static UserJdbcDao userDao;
    private static OrderJdbcDao orderDao;
    private static ValidatorJdbcImpl validator;
    private static double pricePerKilometer = 5;
    private static GoogleMapsAPI googleMapsAPI = new GoogleMapsAPIImpl();
    private static Map<String, User> accessKeys = new HashMap<>();

    private static UserService userService;

    private static Address address1;
    private static Address address2;
    private static Car car;
    private static User passenger1;
    private static User passenger2;
    private static User driver1;
    private static User driver2;
    private static User anonymousUser;
    private static Order order1;
    private static Order order2;
    private static String accessKey;

    @BeforeClass
    public static void beforeClass() {
        userDao = new UserJdbcDao();
        orderDao = new OrderJdbcDao(userDao);
        validator = new ValidatorJdbcImpl(userDao);
        userService = new UserServiceJdbcImpl(userDao, orderDao, validator);

        //delete after sharing to methods below
        address1 = new Address("Ukraine", "Kiev", "Khreschatik", "5");
        address2 = new Address("Ukraine", "Kiev", "Zhukova", "51");

        car = new Car("sedan", "skoda rapid", "2233");

        passenger1 = new User(UserIdentifier.P, "1234", "test", "Vasya1", address1);
        passenger2 = new User(UserIdentifier.P, "4321", "test", "Vasya2", address2);

        driver1 = new User(UserIdentifier.D, "5678", "test", "Petya1", car);
        driver2 = new User(UserIdentifier.D, "8765", "test", "Petya2", car);

        anonymousUser = new User(UserIdentifier.A, "0000", "Anonym");

        //test current orders for driver
        order1 = new Order (address1, address2, passenger1, 10, 100, "I have a dog!:)");
        order2 = new Order (address2, address1, passenger2, 10, 100, "I have a cat!:)");
    }

    @Test()
    public void _01registerPassengerPositive() {

        User testPassenger = userDao.createUser(passenger1);
        if (testPassenger != null) {
            passenger1.setId(testPassenger.getId());
        }

        Assert.assertEquals(passenger1, testPassenger);
    }

    /*@Test()
    public void _02registerPassengerNegative() {

        User testPassenger = userDao.createUser(passenger1);
        if (testPassenger != null) {
            passenger1.setId(testPassenger.getId());
        }

        Assert.assertNotEquals(passenger1, testPassenger);
    }*/

    @Test()
    public void _03registerDriverPositive() {

        User testDriver = userDao.createUser(driver1);
        if (testDriver != null) {
            driver1.setId(testDriver.getId());
        }

        Assert.assertEquals(driver1, testDriver);
    }

    /*@Test()
    public void _04registerDriverNegative() {

        User testDriver = userDao.createUser(driver1);
        if (testDriver != null) {
            driver1.setId(testDriver.getId());
        }

        Assert.assertNotEquals(driver1, testDriver);
    }*/

    /*@Test()
    public void _04_1registerAnonymousPositive() {

        User testAnonym = userDao.createUser(anonymousUser);
        if (testAnonym != null) {
            anonymousUser.setId(testAnonym.getId());
        }

        Assert.assertEquals(anonymousUser, testAnonym);
    }*/

    @Test()
    public void _04_2registerAnonymousNegative() {

        User testAnonym = userDao.createUser(anonymousUser);
        if (testAnonym != null) {
            anonymousUser.setId(testAnonym.getId());
        }

        Assert.assertNotEquals(anonymousUser, testAnonym);
    }

    /*@Test()
    public void _05loginUserPositive() {

        User found = null;
        Collection<User> users = userDao.getAllUsers();
        for (User user : users) {
            found = user.getPhone().equals(passenger1.getPhone()) ? user : found ;
        }

        Assert.assertEquals(passenger1, found);
    }*/

    /*@Test()
    public void _06loginUserNegative() {

        User found = null;
        Collection<User> users = userDao.getAllUsers();
        for (User user : users) {
            found = user.getPhone().equals(passenger2.getPhone()) ? user : found ;
        }

        Assert.assertNotEquals(null, found);
    }*/

    /*@Test()
    public void _07makeOrderPositive() {

        Order testOrder = orderDao.create(passenger1, order1);
        if (testOrder != null) {
            order1.setId(testOrder.getId());
            passenger1.getOrderIds().add(order1.getId());
        }

        Assert.assertEquals(order1, testOrder);
    }*/

    /*@Test()
    public void _08makeOrderNegative() {

        Order testOrder = orderDao.create(passenger1, order1);
        if (testOrder != null) {
            order1.setId(testOrder.getId());
            passenger1.getOrderIds().add(order1.getId());
        }

        Assert.assertNotEquals(order1, testOrder);
    }*/

    /*@Test()
    public void _09makeOrderAnonymousPositive() {

        Order testOrder = orderDao.create(anonymousUser, order1);
        if (testOrder != null) {
            order1.setId(testOrder.getId());
        }

        Assert.assertEquals(order1, testOrder);
    }*/

    @Test()
    public void _10makeOrderAnonymousNegative() {

        Order testOrder = orderDao.create(anonymousUser, order1);
        if (testOrder != null) {
            order1.setId(testOrder.getId());
        }

        Assert.assertNotEquals(order1, testOrder);
    }

    /*@Test()
    public void _11getOrderInfoPositive() {

        Order testOrder = orderDao.findById(order1.getId());

        Assert.assertEquals(order1, testOrder);
    }*/

    @Test()
    public void _12getOrderInfoNegative() {

        passenger2 = userDao.createUser(passenger2);
        Order testOrder = orderDao.create(passenger2, order2);

        Assert.assertNotEquals(order1, testOrder);
    }

    /*@Test()
    public void _13getLastOrderInfoPositive() {

        List<Order> allUserOrders = orderDao.getOrdersOfUser(passenger1);

        Order testOrder = allUserOrders.get(allUserOrders.size() - 1);

        Assert.assertEquals(order1, testOrder);
    }*/

    @Test()
    public void _14getLastOrderInfoNegative() {

        Order testOrder = orderDao.create(passenger1, order2);
        if (testOrder != null) {
            order2.setId(testOrder.getId());
        }

        List<Order> allUserOrders = orderDao.getOrdersOfUser(passenger1);

        testOrder = allUserOrders.get(allUserOrders.size() - 1);;

        Assert.assertNotEquals(order1, testOrder);
    }

    /*@Test()
    public void _15cancelOrderPositive() {

        order1.setOrderStatus(OrderStatus.CANCELLED);
        orderDao.update(order1);
        Order testOrder = orderDao.findById(order1.getId());

        Assert.assertEquals(OrderStatus.CANCELLED, testOrder.getOrderStatus());
    }*/

    /*@Test()
    public void _16cancelOrderNegative() {

        order1.setOrderStatus(OrderStatus.CANCELLED);
        orderDao.update(order1);
        Order testOrder = orderDao.findById(order1.getId());

        Assert.assertNotEquals(OrderStatus.CANCELLED, testOrder.getOrderStatus());
    }*/

    /*@Test()
    public void _17closeOrderPositive() {

        order1.setOrderStatus(OrderStatus.DONE);
        orderDao.update(order1);
        Order testOrder = orderDao.findById(order1.getId());

        Assert.assertEquals(OrderStatus.DONE, testOrder.getOrderStatus());
    }*/

    /*@Test()
    public void _18closeOrderNegative() {

        order1.setOrderStatus(OrderStatus.DONE);
        orderDao.update(order1);
        Order testOrder = orderDao.findById(order1.getId());

        Assert.assertNotEquals(OrderStatus.DONE, testOrder.getOrderStatus());
    }*/

    @Test()
    public void _19takeOrderPositive() {;

        Order testOrder = orderDao.findById(order1.getId());

        testOrder.setDriver(driver1);
        testOrder.setOrderStatus(OrderStatus.IN_PROGRESS);

        orderDao.addToDriver(driver1, testOrder);
        orderDao.update(testOrder);

        Assert.assertEquals(OrderStatus.IN_PROGRESS, testOrder.getOrderStatus());
        Assert.assertEquals(true, testOrder.getDriver().equals(driver1));
    }

    /*@Test()
    public void _20takeOrderNegative() {

        Order testOrder = orderDao.findById(order1.getId());

        testOrder.setDriver(driver1);
        //testOrder.setOrderStatus(OrderStatus.IN_PROGRESS);

        orderDao.addToDriver(driver1, testOrder);
        orderDao.update(testOrder);

        driver1.getOrderIds().add(testOrder.getId());

        Assert.assertNotEquals(OrderStatus.IN_PROGRESS, testOrder.getOrderStatus());
        Assert.assertNotEquals(true, testOrder.getDriver().equals(driver1));
    }*/

    /*@Test()
    public void _21getUserPositive() {

        User testUser = accessKeys.get(accessKey);

        Assert.assertEquals(passenger1, testUser);
    }*/

    @Test()
    public void _22getUserNegative() {

        User testUser = accessKeys.get(accessKey);

        Assert.assertNotEquals(passenger2, testUser);
    }

    /*@Test()
    public void _23getAllOrdersUserPositive() {

        List<Order> testList = orderDao.getOrdersOfUser(accessKeys.get(accessKey));
        int count = 0;

        for (int i = 0; i < testList.size(); i++) {
            if (testList.get(i).getId() == passenger1.getOrderIds().get(i)) {
                count++;
            }
        }

        Assert.assertEquals(true, count == testList.size());
    }*/

    /*@Test()
    public void _24getAllOrdersUserNegative() {

        List<Order> testList = orderDao.getOrdersOfUser(null);
        int count = 0;

        for (int i = 0; i < testList.size(); i++) {
            if (testList.get(i).getId() == passenger2.getOrderIds().get(i)) {
                count++;
            }
        }

        Assert.assertNotEquals(true, count == testList.size());
    }*/

    @Test()
    public void _25updateUserPositive() {

        passenger1.setPhone("upd_1234");
        passenger1.setName("upd_Vasya");
        passenger1.setPass("upd_test");
        User testUser = userDao.updateUser(passenger1);

        Assert.assertEquals(passenger1, testUser);
    }

    @Test()
    public void _26updateUserNegative() {

        User oldUser = userDao.findById(passenger1.getId());

        passenger1.setPhone("1234");
        passenger1.setName("Vasya");
        passenger1.setPass("test");
        User testUser = userDao.updateUser(passenger1);

        Assert.assertNotEquals(oldUser, testUser);
    }

    /*@Test()
    public void _27deleteUserPositive() {

        User userForDelete = userDao.createUser(passenger1);
        int id = userForDelete.getId();

        userDao.deleteUser(id);
        Collection<User> list = userDao.getAllUsers();
        User testUser = null;

        for (User user : list) {
            if (user.getId() == id) {
                testUser = user;
            }
        }

        Assert.assertEquals(null, testUser);
    }*/

    /*@Test()
    public void _28deleteUserNegative() {

        User createdUser = userDao.createUser(passenger1);
        userDao.deleteUser(createdUser.getId());
        Collection<User> list = userDao.getAllUsers();
        User testUser = null;

        for (User user : list) {
            if (user.getId() == passenger1.getId()) {
                testUser = user;
            }
        }

        Assert.assertNotEquals(passenger1, testUser);
    }*/

    @Test
    public void _27updateAddress(){

    }

}
