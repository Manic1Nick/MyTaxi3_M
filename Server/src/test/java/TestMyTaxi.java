import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.artcode.taxi.dao.OrderJdbcDao;
import ua.artcode.taxi.dao.UserJdbcDao;
import ua.artcode.taxi.model.*;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.service.UserServiceJdbcImpl;
import ua.artcode.taxi.service.ValidatorJdbcImpl;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPI;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPIImpl;

import java.util.*;

public class TestMyTaxi {

    private static UserJdbcDao userDao;
    private static OrderJdbcDao orderDao;
    private static ValidatorJdbcImpl validator;
    private static double pricePerKilometer = 5;
    private static GoogleMapsAPI googleMapsAPI = new GoogleMapsAPIImpl();
    private static Map<String, User> accessKeys = new HashMap<>();

    private static UserService userService;

    private static User passenger;
    private static User driver;
    private static User anonymousUser;
    private static Order order;
    private static String accessKey;

    @BeforeClass
    public static void beforeClass() {
        userDao = new UserJdbcDao();
        orderDao = new OrderJdbcDao();
        validator = new ValidatorJdbcImpl(userDao);
        userService = new UserServiceJdbcImpl(userDao, orderDao, validator);

        //delete after sharing to methods below
        passenger = new User(UserIdentifier.P,
                "1234", "test", "Vasya", new Address("Ukraine", "Kiev", "Khreschatik", "5"));

        driver = new User(UserIdentifier.D,
                "5678", "test", "Petya", new Car("sedan", "skoda rapid", "2233"));

        anonymousUser = new User(UserIdentifier.A, "0000", "Anonym");

        //test current orders for driver
        order = new Order (new Address("Ukraine", "Kiev", "Zhukova", "51"),
                new Address("Ukraine", "Kiev", "Khreschatik", "5"), passenger, 10, 100, "I have a dog!:)");
    }

    @Test()
    public void _01registerPassengerPositive() {

        User testPassenger = userDao.createUser(passenger);
        passenger.setId(testPassenger.getId());

        Assert.assertEquals(passenger, testPassenger);
    }

    @Test()
    public void _02registerPassengerNegative() {

        User testPassenger = userDao.createUser(null);
        Assert.assertNotEquals(passenger, testPassenger);
    }

    @Test()
    public void _03registerDriverPositive() {

        User testDriver = userDao.createUser(driver);
        driver.setId(testDriver.getId());

        Assert.assertEquals(driver, testDriver);
    }

    @Test()
    public void _04registerDriverNegative() {

        User testDriver = userDao.createUser(null);
        Assert.assertNotEquals(driver, testDriver);
    }

    @Test()
    public void _05loginUserPositive() {

        User found = null;
        Collection<User> users = userDao.getAllUsers();
        for (User user : users) {
            found = user.getPhone().equals(passenger.getPhone()) ? user : found ;
        }
        Assert.assertEquals(passenger, found);
    }

    @Test()
    public void _06loginUserNegative() {

        User found = null;
        Collection<User> users = userDao.getAllUsers();
        for (User user : users) {
            found = user.getPhone().equals(null) ? user : found ;
        }
        accessKey = UUID.randomUUID().toString();
        accessKeys.put(accessKey, found);

        Assert.assertNotEquals(passenger, found);
    }

    @Test()
    public void _07makeOrderPositive() {

        Order testOrder = orderDao.create(passenger, order);
        order.setId(testOrder.getId());
        passenger.getOrderIds().add(testOrder.getId());

        Assert.assertEquals(order, testOrder);
    }

    @Test()
    public void _08makeOrderNegative() {

        Order testOrder1 = orderDao.create(null, order);
        order.setId(testOrder1.getId());
        passenger.getOrderIds().add(testOrder1.getId());

        Assert.assertNotEquals(order, testOrder1);

        Order testOrder2 = orderDao.create(passenger, null);
        order.setId(testOrder2.getId());
        passenger.getOrderIds().add(testOrder2.getId());

        Assert.assertNotEquals(order, testOrder2);
    }

    @Test()
    public void _07makeOrderAnonymousPositive() {

        Order testOrder = orderDao.create(anonymousUser, order);
        order.setId(testOrder.getId());

        Assert.assertEquals(order, testOrder);
    }

    @Test()
    public void _08makeOrderAnonymousNegative() {

        Order testOrder1 = orderDao.create(null, order);
        order.setId(testOrder1.getId());

        Assert.assertNotEquals(testOrder1, order);

        Order testOrder2 = orderDao.create(anonymousUser, null);
        order.setId(testOrder2.getId());

        Assert.assertNotEquals(order, testOrder2);
    }

    @Test()
    public void _09getOrderInfoPositive() {

        Order testOrder = orderDao.findById(order.getId());

        Assert.assertEquals(order, testOrder);
    }

    @Test()
    public void _10getOrderInfoNegative() {

        Order testOrder = orderDao.findById(-1);

        Assert.assertNotEquals(order, testOrder);
    }

    @Test()
    public void _11getLastOrderInfoPositive() {

        List<Order> allUserOrders = userDao.getOrdersOfUser(passenger);

        Order testOrder = allUserOrders.get(allUserOrders.size() - 1);

        Assert.assertEquals(order, testOrder);
    }

    @Test()
    public void _12getLastOrderInfoNegative() {

        List<Order> allUserOrders = userDao.getOrdersOfUser(passenger);

        Order testOrder = allUserOrders.get(-1);

        Assert.assertNotEquals(order, testOrder);
    }

    @Test()
    public void _13cancelOrderPositive() {

        Order testOrder = orderDao.findById(order.getId());
        testOrder.setOrderStatus(OrderStatus.CANCELLED);
        orderDao.update(testOrder);
        testOrder = orderDao.findById(testOrder.getId());

        Assert.assertEquals(OrderStatus.CANCELLED, testOrder.getOrderStatus());
    }

    @Test()
    public void _14cancelOrderNegative() {

        Order testOrder = orderDao.findById(order.getId());
        testOrder.setOrderStatus(null);
        orderDao.update(testOrder);
        testOrder = orderDao.findById(testOrder.getId());

        Assert.assertNotEquals(OrderStatus.CANCELLED, testOrder.getOrderStatus());
    }

    @Test()
    public void _15closeOrderPositive() {

        Order testOrder = orderDao.findById(order.getId());
        testOrder.setOrderStatus(OrderStatus.DONE);
        orderDao.update(testOrder);
        testOrder = orderDao.findById(testOrder.getId());

        Assert.assertEquals(OrderStatus.DONE, testOrder.getOrderStatus());
    }

    @Test()
    public void _16closeOrderNegative() {

        Order testOrder = orderDao.findById(order.getId());
        testOrder.setOrderStatus(null);
        orderDao.update(testOrder);
        testOrder = orderDao.findById(testOrder.getId());

        Assert.assertNotEquals(OrderStatus.DONE, testOrder.getOrderStatus());
    }

    @Test()
    public void _17takeOrderPositive() {

        Order testOrder = orderDao.findById(order.getId());

        testOrder.setDriver(driver);
        testOrder.setOrderStatus(OrderStatus.IN_PROGRESS);

        orderDao.addToDriver(driver, testOrder);
        orderDao.update(testOrder);

        driver.getOrderIds().add(testOrder.getId());

        Assert.assertEquals(OrderStatus.IN_PROGRESS, testOrder.getOrderStatus());
        Assert.assertEquals(true, testOrder.getDriver().equals(driver));
    }

    @Test()
    public void _18takeOrderNegative() {

        Order testOrder = orderDao.findById(order.getId());

        testOrder.setDriver(null);
        testOrder.setOrderStatus(null);

        orderDao.addToDriver(driver, testOrder);
        orderDao.update(testOrder);

        driver.getOrderIds().add(testOrder.getId());

        Assert.assertNotEquals(OrderStatus.IN_PROGRESS, testOrder.getOrderStatus());
        Assert.assertNotEquals(true, testOrder.getDriver().equals(driver));
    }

    @Test()
    public void _19getUserPositive() {

        User testUser = accessKeys.get(accessKey);

        Assert.assertEquals(passenger, testUser);
    }

    @Test()
    public void _20getUserNegative() {

        User testUser = accessKeys.get(null);

        Assert.assertEquals(passenger, testUser);
    }

    @Test()
    public void _21getAllOrdersUserPositive() {

        List<Order> testList = userDao.getOrdersOfUser(accessKeys.get(accessKey));
        int count = 0;

        for (int i = 0; i < testList.size(); i++) {
            if (testList.get(i).getId() == passenger.getOrderIds().get(i)) {
                count++;
            }
        }

        Assert.assertEquals(true, count == testList.size());
    }

    @Test()
    public void _22getAllOrdersUserNegative() {

        List<Order> testList = userDao.getOrdersOfUser(null);
        int count = 0;

        for (int i = 0; i < testList.size(); i++) {
            if (testList.get(i).getId() == passenger.getOrderIds().get(i)) {
                count++;
            }
        }

        Assert.assertNotEquals(true, count == testList.size());
    }

    @Test()
    public void _23updateUserPositive() {

        passenger.setPhone("4321");
        passenger.setName("Name");
        passenger.setPass("Pass");
        User testUser = userDao.updateUser(passenger);

        Assert.assertEquals(passenger, testUser);
    }

    @Test()
    public void _24updateUserNegative() {

        User testUser = userDao.updateUser(null);

        Assert.assertNotEquals(passenger, testUser);
    }

    @Test()
    public void _25deleteUserPositive() {

        userDao.deleteUser(passenger.getId());
        Collection<User> list = userDao.getAllUsers();
        User testUser = null;

        for (User user : list) {
            if (user.getId() == passenger.getId()) {
                testUser = user;
            }
        }

        Assert.assertEquals(null, testUser);
    }

    @Test()
    public void _26deleteUserNegative() {

        userDao.deleteUser(-1);
        Collection<User> list = userDao.getAllUsers();
        User testUser = null;

        for (User user : list) {
            if (user.getId() == passenger.getId()) {
                testUser = user;
            }
        }

        Assert.assertNotEquals(null, testUser);
    }

}
