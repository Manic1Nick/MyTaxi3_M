package ua.artcode.taxi.run;

import com.google.gson.Gson;
import ua.artcode.taxi.dao.*;
import ua.artcode.taxi.exception.*;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.service.UserServiceJdbcImpl;
import ua.artcode.taxi.service.ValidatorJdbcImpl;
import ua.artcode.taxi.to.Message;
import ua.artcode.taxi.to.MessageBody;
import ua.artcode.taxi.utils.ReflectionFormatter;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RunServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(43009);

        Gson gson = new Gson();

        //create test data
        AddressDao addressDao = new AddressDao();
        CarDao carDao = new CarDao();
        UserDao userDao = new UserJdbcDao(addressDao, carDao);
        OrderDao orderDao = new OrderJdbcDao(userDao, addressDao);
        ValidatorJdbcImpl validator = new ValidatorJdbcImpl(userDao);

        UserService userService = new UserServiceJdbcImpl(userDao, orderDao, validator);
/*
        User passenger1 = new User(UserIdentifier.P,
                "1234", "test", "Vasya", new Address("Ukraine", "Kiev", "Khreschatik", "5"));
        User passenger2 = new User(UserIdentifier.P,
                "1111", "test1", "Ivan", new Address("Ukraine", "Kiev", "Zhukova", "51"));
        userDao.createUser(passenger1);
        userDao.createUser(passenger2);

        User driver1 = new User(UserIdentifier.D,
                "5678", "test", "Petya", new Car("sedan", "skoda rapid", "2233"));
        User driver2 = new User(UserIdentifier.D,
                "2222", "test1", "Dima", new Car("pickup", "mitsubishi l200", "2346"));
        userDao.createUser(driver1);
        userDao.createUser(driver2);

        //test current orders for driver
        Order order1 = new Order (new Address("Ukraine", "Kiev", "Zhukova", "51"),
                new Address("Ukraine", "Kiev", "Khreschatik", "5"), passenger1, 10, 100, "I have a dog!:)");
        Order order2 = new Order(new Address("Ukraine", "Kiev", "Khreschatik", "11"),
                new Address("Ukraine", "Kiev", "Khreschatik", "5"), passenger2, 1, 10, "I have a cat!:(");
        Order order3 = new Order (new Address("Ukraine", "Kiev", "Starokievskaya", "1"),
                new Address("Ukraine", "Kiev", "Khreschatik", "5"), passenger1, 20, 200, "");
        Order order4 = new Order(new Address("Ukraine", "Kiev", "Perova", "10"),
                new Address("Ukraine", "Kiev", "Khreschatik", "5"), passenger2, 15, 150, "");
        Order order5 = new Order (new Address("Ukraine", "Kiev", "Shevchenka", "30"),
                new Address("Ukraine", "Kiev", "Khreschatik", "5"), passenger2, 2, 20, "");
        Order order6 = new Order(new Address("Ukraine", "Kiev", "Liskovskaya", "33"),
                new Address("Ukraine", "Kiev", "Khreschatik", "5"), passenger1, 30, 250, "");

        orderDao.create(passenger1, order1);
        orderDao.create(passenger2, order2);
        orderDao.create(passenger1, order3);
        orderDao.create(passenger2, order4);
        orderDao.create(passenger2, order5);
        orderDao.create(passenger1, order6);
*/

        while(true){
            // waiting for new client
            Socket clientSocket = serverSocket.accept();

            PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));


            Runnable clientThreadLogic = new ClientThreadLogic(gson, userService, pw, bf) ;
            Thread clientThread = new Thread(clientThreadLogic);
            clientThread.start();
        }
    }


    public static String getMenu(){

        return "1. Add user \n" + "2. Exit\n";
    }
}

class ClientThreadLogic implements Runnable {


    private Gson gson;
    private UserService userService;

    private PrintWriter pw;
    private BufferedReader bf;

    public ClientThreadLogic(Gson gson, UserService userService,
                             PrintWriter pw, BufferedReader bf) {
        this.gson = gson;
        this.userService = userService;
        this.pw = pw;
        this.bf = bf;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            String requestBody = null;
            try {
                requestBody = bf.readLine() + "\n";
            } catch (IOException e) {
                e.printStackTrace();
            }

            Message message = gson.fromJson(requestBody, Message.class);

            //registerPassenger
            if ("registerPassenger".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Map<String, String> mapForNewUser = new HashMap<>();
                for (String key : map.keySet()) {
                    mapForNewUser.put(key, map.get(key).toString());
                }

                try {
                    User newUser = userService.registerPassenger(mapForNewUser);

                    Message responseMessage = new Message();
                    MessageBody messageBody = new MessageBody(ReflectionFormatter.userToJsonMap(newUser));
                    responseMessage.setMessageBody(messageBody);

                    pw.println(gson.toJson(responseMessage));
                    pw.flush();

                } catch (RegisterException e) {
                    pw.println(e);
                    pw.flush();
                }
            }

            //registerDriver
            if ("registerDriver".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Map<String, String> mapForNewUser = new HashMap<>();
                for (String key : map.keySet()) {
                    mapForNewUser.put(key, map.get(key).toString());
                }

                try {
                    User newUser = userService.registerDriver(mapForNewUser);

                    Message responseMessage = new Message();
                    MessageBody messageBody = new MessageBody(ReflectionFormatter.userToJsonMap(newUser));
                    responseMessage.setMessageBody(messageBody);

                    pw.println(gson.toJson(responseMessage));
                    pw.flush();

                } catch (RegisterException e) {
                    pw.println(e);
                    pw.flush();
                }
            }

            //login
            if ("login".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Object phone = map.get("phone");
                Object pass = map.get("pass");

                try {
                    String accessKey = userService.login(phone.toString(), pass.toString());

                    Message responseMessage = new Message();
                    MessageBody messageBody = new MessageBody();

                    messageBody.getMap().put("accessKey", accessKey);
                    responseMessage.setMessageBody(messageBody);

                    pw.println(gson.toJson(responseMessage));
                    pw.flush();

                } catch (LoginException e) {
                    pw.println(e);
                    pw.flush();
                } catch (Exception e) {
                    pw.println(e);
                    pw.flush();
                }
            }

            //makeOrder
            if ("makeOrder".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Object accessToken = map.get("accessToken");
                Object addressFrom = map.get("addressFrom");
                Object addressTo = map.get("addressTo");
                Object messageText = map.get("messageText");
                try {
                    Order newOrder = userService.makeOrder(
                            accessToken.toString(),
                            addressFrom.toString(),
                            addressTo.toString(),
                            messageText.toString());

                    Message responseMessage = new Message();
                    MessageBody messageBody = new MessageBody(ReflectionFormatter.orderToJsonMap(newOrder));
                    responseMessage.setMessageBody(messageBody);

                    pw.println(gson.toJson(responseMessage));
                    pw.flush();

                } catch (OrderMakeException e) {
                    pw.println(e);
                    pw.flush();
                } catch (UserNotFoundException e) {
                    pw.println(e);
                    pw.flush();
                } catch (InputDataWrongException e) {
                    pw.println(e);
                    pw.flush();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }

            //makerOrderAnonymous
            if ("makeOrderAnonymous".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Object phone = map.get("phone");
                Object name = map.get("name");
                Object addressFrom = map.get("addressFrom");
                Object addressTo = map.get("addressTo");
                Object messageText = map.get("message");
                try {
                    Order newOrder = userService.makeOrderAnonymous(
                            phone.toString(),
                            name.toString(),
                            addressFrom.toString(),
                            addressTo.toString(),
                            messageText.toString());

                    Message responseMessage = new Message();
                    MessageBody messageBody = new MessageBody(ReflectionFormatter.orderToJsonMap(newOrder));
                    responseMessage.setMessageBody(messageBody);

                    pw.println(gson.toJson(responseMessage));
                    pw.flush();

                } catch (OrderMakeException e) {
                    pw.println(e);
                    pw.flush();
                } catch (InputDataWrongException e) {
                    pw.println(e);
                    pw.flush();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }

            //calculateOrder
            if ("calculateOrder".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Object addressFrom = map.get("addressFrom");
                Object addressTo = map.get("addressTo");
                try {
                    Map<String, Object> mapForMessage = userService.calculateOrder(
                            addressFrom.toString(), addressTo.toString());

                    Message responseMessage = new Message();
                    MessageBody messageBody = new MessageBody(mapForMessage);
                    responseMessage.setMessageBody(messageBody);

                    pw.println(gson.toJson(responseMessage));
                    pw.flush();

                } catch (InputDataWrongException e) {
                    pw.println(e);
                    pw.flush();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }

            //getOrderInfo
            if ("getOrderInfo".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Object orderId = map.get("orderId");
                try {
                    Order foundOrder = userService.getOrderInfo(
                            Long.parseLong(orderId.toString()));

                    Message responseMessage = new Message();
                    MessageBody messageBody = new MessageBody(ReflectionFormatter.orderToJsonMap(foundOrder));
                    responseMessage.setMessageBody(messageBody);

                    pw.println(gson.toJson(responseMessage));
                    pw.flush();

                } catch (OrderNotFoundException e) {
                    pw.println(e);
                    pw.flush();
                }
            }

            //getLastOrderInfo
            if ("getLastOrderInfo".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Object accessToken = map.get("accessToken");
                try {
                    Order foundLastOrder = userService.getLastOrderInfo(
                            accessToken.toString());

                    Message responseMessage = new Message();
                    MessageBody messageBody = new MessageBody(ReflectionFormatter.orderToJsonMap(foundLastOrder));
                    responseMessage.setMessageBody(messageBody);

                    pw.println(gson.toJson(responseMessage));
                    pw.flush();

                } catch (OrderNotFoundException e) {
                    pw.println(e);
                    pw.flush();
                } catch (UserNotFoundException | NullPointerException e) {
                    pw.println(e);
                    pw.flush();
                }
            }

            //cancelOrder
            if ("cancelOrder".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Object orderId = map.get("orderId");
                try {
                    Order cancelledOrder = userService.cancelOrder(
                            Long.parseLong(orderId.toString()));

                    Message responseMessage = new Message();
                    MessageBody messageBody = new MessageBody(ReflectionFormatter.orderToJsonMap(cancelledOrder));
                    responseMessage.setMessageBody(messageBody);

                    pw.println(gson.toJson(responseMessage));
                    pw.flush();

                } catch (OrderNotFoundException e) {
                    pw.println(e);
                    pw.flush();
                }
            }

            //closeOrder
            if ("closeOrder".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Object accessToken = map.get("accessToken");
                Object orderId = map.get("orderId");
                try {
                    Order closedOrder = userService.closeOrder(
                            accessToken.toString(),
                            Long.parseLong(orderId.toString()));

                    Message responseMessage = new Message();
                    MessageBody messageBody = new MessageBody(ReflectionFormatter.orderToJsonMap(closedOrder));
                    responseMessage.setMessageBody(messageBody);

                    pw.println(gson.toJson(responseMessage));
                    pw.flush();

                } catch (OrderNotFoundException e) {
                    pw.println(e);
                    pw.flush();
                } catch (WrongStatusOrderException e) {
                    pw.println(e);
                    pw.flush();
                } catch (DriverOrderActionException e) {
                    pw.println(e);
                    pw.flush();
                }
            }

            //takeOrder
            if ("takeOrder".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Object accessToken = map.get("accessToken");
                Object orderId = map.get("orderId");
                try {
                    Order takenOrder = userService.takeOrder(
                            accessToken.toString(),
                            Double.valueOf(orderId + "").longValue());

                    Message responseMessage = new Message();
                    MessageBody messageBody = new MessageBody(ReflectionFormatter.orderToJsonMap(takenOrder));
                    responseMessage.setMessageBody(messageBody);

                    pw.println(gson.toJson(responseMessage));
                    pw.flush();

                } catch (OrderNotFoundException e) {
                    pw.println(e);
                    pw.flush();
                } catch (WrongStatusOrderException e) {
                    pw.println(e);
                    pw.flush();
                } catch (DriverOrderActionException e) {
                    pw.println(e);
                    pw.flush();
                }
            }

            //getMapDistancesToDriver
            if ("getMapDistancesToDriver".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Object orderStatus = map.get("orderStatus");
                Object addressDriver = map.get("addressDriver");

                Map<Integer, Order> ordersMap = null;
                try {
                    ordersMap = userService.getMapDistancesToDriver(
                            orderStatus.toString(),
                            addressDriver.toString());

                    Map<String, Object> mapForMessage = new HashMap<>();
                    for (Integer key : ordersMap.keySet()) {
                        Map<String, Object> concreteOrder = ReflectionFormatter.orderToJsonMap(ordersMap.get(key));
                        Message concreteMessage = new Message();
                        MessageBody concreteMessageBody = new MessageBody(concreteOrder);
                        concreteMessage.setMessageBody(concreteMessageBody);

                        mapForMessage.put(key + "", gson.toJson(concreteMessage));
                    }

                    Message responseMessage = new Message();
                    MessageBody messageBody = new MessageBody(mapForMessage);
                    responseMessage.setMessageBody(messageBody);

                    pw.println(gson.toJson(responseMessage));
                    pw.flush();

                } catch (InputDataWrongException e) {
                    pw.println(e);
                    pw.flush();
                }


            }

            //getUser
            if ("getUser".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Object accessToken = map.get("accessToken");

                User foundUser = userService.getUser(accessToken.toString());

                Message responseMessage = new Message();
                MessageBody messageBody = new MessageBody(ReflectionFormatter.userToJsonMap(foundUser));
                responseMessage.setMessageBody(messageBody);

                pw.println(gson.toJson(responseMessage));
                pw.flush();
            }

            //getAllOrdersUser
            if ("getAllOrdersUser".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Object accessToken = map.get("accessToken");

                List<Order> orders = userService.getAllOrdersUser(accessToken.toString());

                Map<String, Object> mapForMessage = new HashMap<>();
                for (int i = 0; i < orders.size(); i++) {
                    Map<String, Object> concreteOrder = ReflectionFormatter.orderToJsonMap(orders.get(i));
                    Message concreteMessage = new Message();
                    MessageBody concreteMessageBody = new MessageBody(concreteOrder);
                    concreteMessage.setMessageBody(concreteMessageBody);

                    mapForMessage.put(orders.get(i).getId() + "", gson.toJson(concreteMessage));
                }

                Message responseMessage = new Message();
                MessageBody messageBody = new MessageBody(mapForMessage);
                responseMessage.setMessageBody(messageBody);

                pw.println(gson.toJson(responseMessage));
                pw.flush();
            }

            //updateUser
            if ("updateUser".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Map<String, String> mapForNewUser = new HashMap<>();
                for (String key : map.keySet()) {
                    mapForNewUser.put(key, map.get(key).toString());
                }
                Object accessToken = map.get("accessToken");

                try {
                    User updatedUser = userService.updateUser(mapForNewUser, accessToken.toString());

                    Message responseMessage = new Message();
                    MessageBody messageBody = new MessageBody(ReflectionFormatter.userToJsonMap(updatedUser));
                    responseMessage.setMessageBody(messageBody);

                    pw.println(gson.toJson(responseMessage));
                    pw.flush();

                } catch (RegisterException e) {
                    pw.println(e);
                    pw.flush();
                }
            }

            //deleteUser
            if ("deleteUser".equals(message.getMethodName())) {
                Map<String, Object> map = message.getMessageBody().getMap();
                Object accessToken = map.get("accessToken");

                User deletedUser = userService.deleteUser(
                        accessToken.toString());

                Message responseMessage = new Message();
                MessageBody messageBody = new MessageBody(ReflectionFormatter.userToJsonMap(deletedUser));
                responseMessage.setMessageBody(messageBody);

                pw.println(gson.toJson(responseMessage));
                pw.flush();
            }
        }
    }
}
