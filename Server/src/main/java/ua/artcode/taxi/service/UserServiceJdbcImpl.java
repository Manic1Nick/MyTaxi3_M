package ua.artcode.taxi.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.artcode.taxi.dao.OrderDao;
import ua.artcode.taxi.dao.UserDao;
import ua.artcode.taxi.exception.*;
import ua.artcode.taxi.model.*;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPI;
import ua.artcode.taxi.utils.geolocation.GoogleMapsAPIImpl;
import ua.artcode.taxi.utils.geolocation.Location;

import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service(value = "service")
public class UserServiceJdbcImpl implements UserService {

    private final static Logger LOG = Logger.getLogger(UserServiceJdbcImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    private double pricePerKilometer;
    private GoogleMapsAPI googleMapsAPI;
    private Map<String, User> accessKeys;

    public UserServiceJdbcImpl() {
        pricePerKilometer = Constants.pricePerKilometer;
        googleMapsAPI = new GoogleMapsAPIImpl();
        accessKeys = new ConcurrentHashMap<>();
    }

    public UserServiceJdbcImpl(UserDao userDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        pricePerKilometer = Constants.pricePerKilometer;
        googleMapsAPI = new GoogleMapsAPIImpl();
        accessKeys = new ConcurrentHashMap<>();
    }

    @Override
    public User registerPassenger(Map<String, String> map) throws RegisterException {

        User found = userDao.findByPhone(map.get("phone"));

        if (found != null) {

            LOG.error("RegisterException: failed attempt to register with phone " + map.get("phone"));

            throw new RegisterException("This phone using already");
        }

        User newUser = new User(
                UserIdentifier.P,
                map.get("phone"),
                map.get("pass"),
                map.get("name"),
                new Address(map.get("homeAddress")));

        User createdUser = userDao.createUser(newUser);

        LOG.info("New passenger " + createdUser.getPhone() + " registered");

        return createdUser;
    }

    @Override
    public User registerDriver(Map<String, String> map) throws RegisterException {

        User found = userDao.findByPhone(map.get("phone"));

        if (found != null) {

            LOG.error("RegisterException: failed attempt to register with phone " + map.get("phone"));

            throw new RegisterException("This phone using already");
        }

        Car newCar = new Car(map.get("carType"), map.get("carModel"), map.get("carNumber"));

        User newUser = new User(
                UserIdentifier.D,
                map.get("phone"),
                map.get("pass"),
                map.get("name"),
                newCar);

        User createdUser = userDao.createUser(newUser);

        LOG.info("New driver " + createdUser.getPhone() + " registered");

        return createdUser;
    }

    @Override
    public String login(String phone, String pass) throws Exception {

        User found = userDao.findByPhone(phone);

        if (found == null) {

            LOG.error("LoginException: failed attempt to log in with phone " + phone);

            throw new LoginException("User not found or incorrect password");
        }

        String accessKey = UUID.randomUUID().toString();
        accessKeys.put(accessKey, found);

        LOG.info("User " + phone + " logged in");

        return accessKey;
    }

    @Override
    public Order makeOrder(String accessToken, String lineFrom, String lineTo, String message) throws
            OrderMakeException, UserNotFoundException, InputDataWrongException, UnknownHostException {

        Address from = new Address(lineFrom);
        Address to = new Address(lineTo);
        Order createdOrder = null;

        if (!validateAddress(from) && !validateAddress(to)) {

            LOG.error("InputDataWrongException: wrong input data address");

            throw  new InputDataWrongException("Wrong input data addresses. Can not make order");

        }

        User user = accessKeys.get(accessToken);

        if (user != null) {

            for (Order order : user.getOrdersPassenger()) {
                if (order.getOrderStatus().equals(OrderStatus.NEW) ||
                        order.getOrderStatus().equals(OrderStatus.IN_PROGRESS)) {

                    LOG.error("OrderMakeException: failed attempt to make order by user " + user.getPhone());

                    throw new OrderMakeException("User has orders NEW or IN_PROGRESS already");
                }
            }

            try {
                Location location = googleMapsAPI.findLocation(from.getCountry(), from.getCity(),
                        from.getStreet(), from.getHouseNum());
                Location location1 = googleMapsAPI.findLocation(to.getCountry(), to.getCity(),
                        to.getStreet(), to.getHouseNum());
                int distance = (int) (googleMapsAPI.getDistance(location, location1) / 1000);
                int price = (int) pricePerKilometer * distance + 30;

                message = message == null || message.equals("") ? "" : user.getName() + ": " + message;

                Order newOrder = new Order(from, to, user, distance, price, message);
                newOrder.setOrderStatus(OrderStatus.NEW);
                createdOrder = orderDao.create(newOrder);

                LOG.info("User " + user.getPhone() + " makes new order " + createdOrder.getId());

            } catch (InputDataWrongException | IndexOutOfBoundsException e) {

                LOG.error("InputDataWrongException: errors in calculate locations in Google API");

                throw new InputDataWrongException("Wrong calculation in Google API");
            }
        }
        return createdOrder;
    }

    @Override
    public Order makeOrderAnonymous(String phone, String name, String lineFrom, String lineTo, String message)
            throws OrderMakeException, InputDataWrongException {

        Address from = new Address(lineFrom);
        Address to = new Address(lineTo);
        Order newOrder = null;

        if (!validateAddress(from) && !validateAddress(to)) {

            LOG.error("InputDataWrongException: wrong input data address");

            throw new InputDataWrongException("Wrong input data. Can not make order");
        }


        User user = userDao.findByPhone(phone);

        if (user != null) {
            for (Order order : user.getOrdersPassenger()) {
                if (order.getOrderStatus().equals(OrderStatus.NEW)) {

                    LOG.error("OrderMakeException: failed attempt to make order by user " + phone);

                    throw new OrderMakeException("User has orders NEW already");
                }
            }
        }

        try {
            Location location = googleMapsAPI.findLocation(from.getCountry(), from.getCity(),
                    from.getStreet(), from.getHouseNum());
            Location location1 = googleMapsAPI.findLocation(to.getCountry(), to.getCity(),
                    to.getStreet(), to.getHouseNum());
            int distance = (int) (googleMapsAPI.getDistance(location, location1) / 1000);
            int price = (int) pricePerKilometer * distance + 30;

            User anonymousUser = userDao.createUser(new User(UserIdentifier.A, phone, name));
            newOrder = new Order(from, to, anonymousUser, distance, price, message);
            orderDao.create(newOrder);

        } catch (InputDataWrongException | IndexOutOfBoundsException e) {

            LOG.error("InputDataWrongException: errors in calculate locations in Google API");

            throw new InputDataWrongException("Wrong calculation in Google API");
        }

        LOG.info("User anonymous makes new order " + newOrder.getId());

        return newOrder;
    }

    @Override
    public Map<String, Object> calculateOrder(String lineFrom, String lineTo) throws
            InputDataWrongException, UnknownHostException {

        Address from = new Address(lineFrom);
        Address to = new Address(lineTo);
        Map<String, Object> map = new HashMap<>();

        if (!validateAddress(from) && !validateAddress(to)) {

            LOG.error("InputDataWrongException: wrong input data address");

            throw new InputDataWrongException("Wrong input data. Can not make order");
        }

        try {
            Location location = googleMapsAPI.findLocation(from.getCountry(), from.getCity(),
                    from.getStreet(), from.getHouseNum());
            Location location1 = googleMapsAPI.findLocation(to.getCountry(), to.getCity(),
                    to.getStreet(), to.getHouseNum());
            int distance = ((int) googleMapsAPI.getDistance(location, location1) / 1000);
            int price = (int) pricePerKilometer * distance + 30;

            map.put("distance", distance + "");
            map.put("price", price + "");

        } catch (InputDataWrongException | IndexOutOfBoundsException e) {

            LOG.error("InputDataWrongException: errors in calculate locations in Google API");

            throw new InputDataWrongException("Wrong calculation in Google API");
        }

        LOG.info("Someone calculates order from " + lineFrom + " to " + lineTo);

        return map;
    }

    @Override
    public Order getOrderInfo(long orderId) throws OrderNotFoundException {

        Order found = orderDao.findById(orderId);

        if (found == null) {

            LOG.error("OrderNotFoundException: failed attempt to get info about order with ID " +
                    orderId + " (order not found in data base)");

            throw new OrderNotFoundException("Order not found in data base");
        }

        LOG.info("Information has been requested for order " + found.getId());

        return found;
    }

    @Override
    public Order getLastOrderInfo(String accessToken) throws UserNotFoundException, OrderNotFoundException {

        if (accessToken == null) {

            LOG.error("UserNotFoundException: failed attempt to find user in data base");

            throw new UserNotFoundException("wrong data user");
        }

        List<Order> allUserOrders = getAllOrdersUser(accessToken);

        if (allUserOrders.size() == 0) {

            LOG.error("OrderNotFoundException: failed attempt to get info about last order of user " +
                    accessKeys.get(accessToken).getPhone());

            throw new OrderNotFoundException("User doesn't have any orders");
        }

        Order lastOrder = allUserOrders.get(allUserOrders.size() - 1);

        LOG.info("User " + accessKeys.get(accessToken).getPhone() +
                " get information for his last order " + lastOrder.getId());

        return lastOrder;
    }

    @Override
    public Order cancelOrder(long orderId) throws OrderNotFoundException, WrongStatusOrderException {

        Order foundOrder = orderDao.findById(orderId);

        if (foundOrder == null) {

            LOG.error("OrderNotFoundException: failed attempt to cancel order with ID " +
                    orderId + " (not found in data base)");

            throw new OrderNotFoundException("Order not found in data base");

        } else if (foundOrder.getOrderStatus().equals(OrderStatus.CLOSED) ||
                    foundOrder.getOrderStatus().equals(OrderStatus.CANCELLED)) {

            LOG.error("WrongStatusOrderException: failed attempt to close order with ID " +
                    orderId);

            throw new WrongStatusOrderException("This order has been CLOSED or CANCELLED already");
        }

        foundOrder.setOrderStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = orderDao.update(foundOrder);

        LOG.info("Order " + cancelledOrder.getId() + " was cancelled by user");

        return cancelledOrder;
    }

    @Override
    public Order closeOrder(String accessToken, long orderId) throws OrderNotFoundException,
            WrongStatusOrderException, DriverOrderActionException {

        User user = accessKeys.get(accessToken);
        Order foundOrder = orderDao.findById(orderId);
        List<Order> ordersUser = orderDao.getOrdersOfUser(user);
        Order orderInDriverList = null;

        for (Order order : ordersUser) {
            if (order.getId() == foundOrder.getId()) {
                orderInDriverList = order;
            }
        }

        if (foundOrder == null) {

            LOG.error("OrderNotFoundException: failed attempt to close order with ID " +
                    orderId + " by user " + user.getPhone());

            throw new OrderNotFoundException("Order not found in data base");

        } else if (orderInDriverList == null) {

            LOG.error("DriverOrderActionException: failed attempt to close order with ID " +
                    orderId + " by user " + user.getPhone());

            throw new DriverOrderActionException("Order not found in driver orders list");

        } else if (!orderInDriverList.getOrderStatus().equals(OrderStatus.IN_PROGRESS)) {

            LOG.error("WrongStatusOrderException: failed attempt to close order with ID " +
                    orderId + " by user " + user.getPhone());

            throw new WrongStatusOrderException("This order has wrong status (not IN_PROGRESS)");

        }

        foundOrder.setOrderStatus(OrderStatus.CLOSED);
        Order closedOrder = orderDao.update(foundOrder);

        LOG.info("User " + user.getPhone() + " closed his order " + closedOrder.getId());

        return closedOrder;
    }

    @Override
    public Order takeOrder(String accessToken, long orderId) throws OrderNotFoundException,
            WrongStatusOrderException, DriverOrderActionException {

        User user = accessKeys.get(accessToken);
        Order inProgress = orderDao.findById(orderId);

        for (Order order : user.getOrdersDriver()) {
            if (order.getOrderStatus().equals(OrderStatus.IN_PROGRESS)) {

                LOG.error("DriverOrderActionException: failed attempt to take order with ID " +
                        orderId + " by user " + user.getPhone());

                throw new DriverOrderActionException("Driver has orders IN_PROGRESS already");
            }
        }

        if (inProgress == null) {

            LOG.error("OrderNotFoundException: failed attempt to take order with ID" +
                    orderId + " by user " + user.getPhone());

            throw new OrderNotFoundException("Order not found in data base");

        } else if (!inProgress.getOrderStatus().equals(OrderStatus.NEW)) {

            LOG.error("WrongStatusOrderException: failed attempt to take order with ID " +
                    orderId + " by user " + user.getPhone());

            throw new WrongStatusOrderException("This order has wrong status (not NEW)");

        }

        inProgress.setDriver(user);
        inProgress.setOrderStatus(OrderStatus.IN_PROGRESS);
        Order takenOrder = orderDao.update(inProgress);

        LOG.info("User " + user.getPhone() + " was take order " + takenOrder.getId() + " for execution");

        return takenOrder;
    }

    @Override
    public User getUser(String accessToken) {

        User foundUser = accessKeys.get(accessToken);

        LOG.info("Request user " + foundUser.getPhone());

        return foundUser;
    }

    @Override
    public List<Order> getAllOrdersUser(String accessToken) {

        User user = accessKeys.get(accessToken);

        List<Order> ordersOfUser = userDao.getAllOrdersOfUser(user);

        LOG.info("Get all orders of user " + user.getPhone());

        return ordersOfUser;
    }

    @Override
    public Map<Integer, Order> getMapDistancesToDriver(String orderStatus, String lineAddressDriver)
            throws InputDataWrongException {

        //find all orders with status
        List<Order> orders = getAllOrdersByStatus(Enum.valueOf(OrderStatus.class, orderStatus));

        LOG.info("Found " + orders.size() + " orders with status " + orderStatus);

        //create list of int unique distances
        List<Integer> distancesList = getArrayDistancesToDriver(orders, new Address(lineAddressDriver));
        int[] distances = new int[distancesList.size()];

        //create map of distances
        Map<Integer, Order> mapDistances = new TreeMap<>();
        for (int i = 0; i < distancesList.size(); i++) {
            distances[i] = distancesList.get(i);
            mapDistances.put(distances[i], orders.get(i));
        }

        //sorting map by distances
        Arrays.sort(distances);
        Map<Integer, Order> sortingMapDistances = new TreeMap<>();
        for (int i = 0; i < distances.length; i++) {
            sortingMapDistances.put(distances[i], mapDistances.get(distances[i]));
        }

        LOG.info("Create map of distances from orders to driver address " + lineAddressDriver);

        return sortingMapDistances;
    }

    @Override
    public User updateUser(Map<String, String> map, String accessToken) throws RegisterException {

        User user = accessKeys.get(accessToken);
        UserIdentifier typeUser = user.getIdentifier();
        int idUser = user.getId();

        User found = userDao.findByPhone(map.get("phone"));

        if (found != null &&
                user.getId() != found.getId()) {

            LOG.error("RegisterException: failed attempt to update user " +
                    user.getPhone() + " (phone " + map.get("phone") + " already in use by another user)");

            throw new RegisterException("This phone is already in use by another user");

        } else {

            //create user for update
            User newUser = new User(typeUser, map.get("phone"), map.get("name"));
            newUser.setId(idUser);
            newUser.setPass(map.get("pass"));
            if (typeUser.equals(UserIdentifier.P)) {
                newUser.setHomeAddress(new Address(map.get("homeAddress")));
            } else if (typeUser.equals(UserIdentifier.D)) {
                newUser.setCar(new Car(map.get("carType"), map.get("carModel"), map.get("carNumber")));
            }

            User updatedUser = userDao.updateUser(newUser);
            accessKeys.put(accessToken, updatedUser);

            LOG.info("Change registered data for user. User " + updatedUser.getPhone() + " was updated");

            return updatedUser;
        }
    }

    @Override
    public User deleteUser(String accessToken) throws WrongStatusOrderException {

        User user = accessKeys.get(accessToken);

        //check open orders of user (NEW or IN_PROGRESS)
        List<Order> orders = getAllOrdersUser(accessToken);
        for (Order order : orders) {
            if (order.getOrderStatus().equals(OrderStatus.NEW) ||
                    order.getOrderStatus().equals(OrderStatus.IN_PROGRESS)) {

                LOG.error("WrongStatusOrderException: failed attempt to delete user " + user.getPhone());

                throw new WrongStatusOrderException
                        ("Can't delete user. User can't has orders with status NEW or IN_PROGRESS");
            }
        }

        User deleteUser = userDao.deleteUser(user.getId());
        accessKeys.remove(accessToken);

        LOG.info("User " + user.getPhone() + " was deleted");

        return deleteUser;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public double getPricePerKilometer() {
        return pricePerKilometer;
    }

    public void setPricePerKilometer(double pricePerKilometer) {
        this.pricePerKilometer = pricePerKilometer;
    }

    public GoogleMapsAPI getGoogleMapsAPI() {
        return googleMapsAPI;
    }

    public void setGoogleMapsAPI(GoogleMapsAPI googleMapsAPI) {
        this.googleMapsAPI = googleMapsAPI;
    }

    public Map<String, User> getAccessKeys() {
        return accessKeys;
    }

    public void setAccessKeys(Map<String, User> accessKeys) {
        this.accessKeys = accessKeys;
    }

    public List<Integer> getArrayDistancesToDriver(List<Order> orders, Address addressDriver)
            throws InputDataWrongException {

        Location locationDriver = googleMapsAPI.findLocation
                (addressDriver.getCountry(), addressDriver.getCity(),
                        addressDriver.getStreet(), addressDriver.getHouseNum());

        //int[] distances = new int[orders.size()];
        List<Integer> distances = new ArrayList<>();

        //increasing distance by 1 or little more meters for unique distances to driver
        int increaseDistance = 1;

        for (Order order : orders) {
            int i = orders.indexOf(order);

            Location locationPassenger = googleMapsAPI.findLocation(
                    orders.get(i).getFrom().getCountry(),
                    orders.get(i).getFrom().getCity(),
                    orders.get(i).getFrom().getStreet(),
                    orders.get(i).getFrom().getHouseNum());

            int distance = new Distance(locationDriver, locationPassenger).calculateDistance();


            if (!distances.isEmpty() && distances.contains(distance)) {
                distances.add(distance + increaseDistance);
                increaseDistance++;

            } else {
                distances.add(distance);
            }
        }

        return distances;
    }

    public List<Order> getAllOrdersByStatus(OrderStatus status) {

        List<Order> ordersByStatus = orderDao.getOrdersByStatus(status);

        LOG.info("Get all order by status " + status.toString());

        return ordersByStatus;
    }

    public class Distance implements Comparable {

        private Location fromLocation;
        private Location toLocation;
        private GoogleMapsAPI googleMapsAPI;

        private int averageSpeedKmH;
        private int timeInMin;

        public Distance() {
        }

        public Distance(Location fromLocation, Location toLocation) {
            this.fromLocation = fromLocation;
            this.toLocation = toLocation;
            googleMapsAPI = new GoogleMapsAPIImpl();
            averageSpeedKmH = Constants.averageSpeedKmH;
        }

        public Location getFromLocation() {
            return fromLocation;
        }

        public void setFromLocation(Location fromLocation) {
            this.fromLocation = fromLocation;
        }

        public Location getToLocation() {
            return toLocation;
        }

        public void setToLocation(Location toLocation) {
            this.toLocation = toLocation;
        }

        public int calculateDistance() throws InputDataWrongException {
            return (int) googleMapsAPI.getDistance(fromLocation, toLocation);
        }

        public void setSpeedKmH(int speedKmH) {
            this.averageSpeedKmH = speedKmH;
        }

        public int getTimeInMin() throws InputDataWrongException {
            return (this.calculateDistance() / 1000) / this.averageSpeedKmH;
        }

        @Override
        public int compareTo(Object o) {

            Distance tmp = (Distance)o;
            try {
                if(this.googleMapsAPI.getDistance(fromLocation, toLocation) <
                        tmp.googleMapsAPI.getDistance(fromLocation, toLocation)) {
                    return -1;

                } else if (this.googleMapsAPI.getDistance(fromLocation, toLocation) >
                        tmp.googleMapsAPI.getDistance(fromLocation, toLocation)) {
                    return 1;
                }

            } catch (InputDataWrongException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    private boolean validateAddress(Address address) throws InputDataWrongException {

        if (address.getCountry().equals("")) {
            throw new InputDataWrongException("Wrong data: country");
            //result = false;
        } else if (address.getCity().equals("")) {
            throw new InputDataWrongException("Wrong data: city");
            //result = false;
        } else if (address.getStreet().equals("")) {
            throw new InputDataWrongException("Wrong data: street");
            //result = false;
        } else if (address.getHouseNum().equals("")) {
            throw new InputDataWrongException("Wrong data: houseNum");
            //result = false;
        }

        return true;
    }
}
