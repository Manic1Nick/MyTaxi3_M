package ua.artcode.taxi.remote;

import com.google.gson.Gson;
import ua.artcode.taxi.exception.*;
import ua.artcode.taxi.model.*;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.to.Message;
import ua.artcode.taxi.to.MessageBody;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteUserService implements UserService {

    private Gson gson;
    private Socket connection;
    private BufferedReader bf;
    private PrintWriter pw;

    public RemoteUserService() {

        gson = new Gson();

        try {
            connection = new Socket("127.0.0.1", 43009);
            bf = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            pw = new PrintWriter(connection.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User registerPassenger(Map<String, String> map) throws RegisterException {

        Message src = new Message();
        src.setMethodName("registerPassenger");

        MessageBody messageBody = new MessageBody();
        for (String key : map.keySet()) {
            messageBody.getMap().put(key, map.get(key));
        }

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (registerPassenger): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("RegisterException")) {
            throw new RegisterException(jsonResponse);
        }

        Message response = gson.fromJson(jsonResponse, Message.class);

        return getUserFromMessage(response.getMessageBody().getMap());
    }

    @Override
    public User registerDriver(Map<String, String> map) throws RegisterException {

        Message src = new Message();
        src.setMethodName("registerDriver");

        MessageBody messageBody = new MessageBody();
        for (String key : map.keySet()) {
            messageBody.getMap().put(key, map.get(key));
        }

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (registerDriver): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("RegisterException")) {
            throw new RegisterException(jsonResponse);
        }

        Message response = gson.fromJson(jsonResponse, Message.class);

        return getUserFromMessage(response.getMessageBody().getMap());
    }

    @Override
    public String login(String phone, String pass) throws LoginException {

        Message src = new Message();
        src.setMethodName("login");

        MessageBody messageBody = new MessageBody();
        messageBody.getMap().put("phone", phone);
        messageBody.getMap().put("pass", pass);

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (login): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("LoginException")) {
            throw new LoginException();
        }

        Message response = gson.fromJson(jsonResponse, Message.class);

        return response.getMessageBody().getMap().get("accessKey").toString();
    }

    @Override
    public Order makeOrder(String accessToken, String lineFrom, String lineTo, String message)
                        throws OrderMakeException, UserNotFoundException, InputDataWrongException {

        Message src = new Message();
        src.setMethodName("makeOrder");

        MessageBody messageBody = new MessageBody();
        messageBody.getMap().put("accessToken", accessToken);
        messageBody.getMap().put("addressFrom", lineFrom);
        messageBody.getMap().put("addressTo", lineTo);
        messageBody.getMap().put("messageText", message);

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (makeOrder): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("OrderMakeException")) {
            throw new OrderMakeException(jsonResponse);
        } else if (jsonResponse.contains("UserNotFoundException")) {
            throw new UserNotFoundException(jsonResponse);
        } else if (jsonResponse.contains("InputDataWrongException")) {
            throw new InputDataWrongException(jsonResponse);
        }

        Message response = gson.fromJson(jsonResponse, Message.class);

        return getOrderFromMessage(response.getMessageBody().getMap());
    }

    @Override
    public Order makeOrderAnonymous(String phone, String name, String from, String to, String message)
                                                    throws OrderMakeException, InputDataWrongException {

        Message src = new Message();
        src.setMethodName("makeOrderAnonymous");

        MessageBody messageBody = new MessageBody();
        messageBody.getMap().put("phone", phone);
        messageBody.getMap().put("name", name);
        messageBody.getMap().put("addressFrom", from);
        messageBody.getMap().put("addressTo", to);
        messageBody.getMap().put("message", message);

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (makeOrderAnonymous): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("OrderMakeException")) {
            throw new OrderMakeException(jsonResponse);
        } else if (jsonResponse.contains("InputDataWrongException")) {
            throw new InputDataWrongException(jsonResponse);
        }

        Message response = gson.fromJson(jsonResponse, Message.class);

        return getOrderFromMessage(response.getMessageBody().getMap());
    }

    @Override
    public Map<String, Object> calculateOrder(String from, String to) throws InputDataWrongException {

        Message src = new Message();
        src.setMethodName("calculateOrder");

        MessageBody messageBody = new MessageBody();
        messageBody.getMap().put("addressFrom", from);
        messageBody.getMap().put("addressTo", to);

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (calculateOrder): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("InputDataWrongException")) {
            throw new InputDataWrongException(jsonResponse);
        }

        Message response = gson.fromJson(jsonResponse, Message.class);

        return response.getMessageBody().getMap();
    }

    @Override
    public Order getOrderInfo(long orderId) throws OrderNotFoundException {

        Message src = new Message();
        src.setMethodName("getOrderInfo");

        MessageBody messageBody = new MessageBody();
        messageBody.getMap().put("orderId", orderId);

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (getOrderInfo): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("OrderNotFoundException")) {
            throw new OrderNotFoundException(jsonResponse);
        }

        Message response = gson.fromJson(jsonResponse, Message.class);

        return getOrderFromMessage(response.getMessageBody().getMap());
    }

    @Override
    public Order getLastOrderInfo(String accessToken) throws UserNotFoundException, OrderNotFoundException {

        Message src = new Message();
        src.setMethodName("getLastOrderInfo");

        MessageBody messageBody = new MessageBody();
        messageBody.getMap().put("accessToken", accessToken);

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (getLastOrderInfo): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("OrderNotFoundException")) {
            throw new OrderNotFoundException(jsonResponse);
        } else if (jsonResponse.contains("UserNotFoundException")) {
            throw new UserNotFoundException(jsonResponse);
        } else if (jsonResponse.contains("NullPointerException")) {
            throw new UserNotFoundException(jsonResponse);
        }

        Message response = gson.fromJson(jsonResponse, Message.class);

        return getOrderFromMessage(response.getMessageBody().getMap());
    }

    @Override
    public Order cancelOrder(long orderId) throws OrderNotFoundException {

        Message src = new Message();
        src.setMethodName("cancelOrder");

        MessageBody messageBody = new MessageBody();
        messageBody.getMap().put("orderId", orderId + "");

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (cancelOrder): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("OrderNotFoundException")) {
            throw new OrderNotFoundException(jsonResponse);
        }

        Message response = gson.fromJson(jsonResponse, Message.class);

        return getOrderFromMessage(response.getMessageBody().getMap());
    }

    @Override
    public Order closeOrder(String accessToken, long orderId) throws
            OrderNotFoundException, WrongStatusOrderException, DriverOrderActionException {

        Message src = new Message();
        src.setMethodName("closeOrder");

        MessageBody messageBody = new MessageBody();
        messageBody.getMap().put("accessToken", accessToken);
        messageBody.getMap().put("orderId", orderId + "");

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (closeOrder): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("OrderNotFoundException")) {
            throw new OrderNotFoundException(jsonResponse);
        } else if (jsonResponse.contains("WrongStatusOrderException")) {
            throw new WrongStatusOrderException(jsonResponse);
        } else if (jsonResponse.contains("DriverOrderActionException")) {
            throw new DriverOrderActionException(jsonResponse);
        }

        Message response = gson.fromJson(jsonResponse, Message.class);

        return getOrderFromMessage(response.getMessageBody().getMap());
    }

    @Override
    public Order takeOrder(String accessToken, long orderId) throws
            OrderNotFoundException, WrongStatusOrderException, DriverOrderActionException {

        Message src = new Message();
        src.setMethodName("takeOrder");

        MessageBody messageBody = new MessageBody();
        messageBody.getMap().put("accessToken", accessToken);
        messageBody.getMap().put("orderId", orderId);

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (takeOrder): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("OrderNotFoundException")) {
            throw new OrderNotFoundException(jsonResponse);
        } else if (jsonResponse.contains("WrongStatusOrderException")) {
            throw new WrongStatusOrderException(jsonResponse);
        } else if (jsonResponse.contains("DriverOrderActionException")) {
            throw new DriverOrderActionException(jsonResponse);
        }

        Message response = gson.fromJson(jsonResponse, Message.class);

        return getOrderFromMessage(response.getMessageBody().getMap());
    }

    @Override
    public Map<Integer, Order> getMapDistancesToDriver(String orderStatus, String lineAddressDriver)
                                                                                throws InputDataWrongException {

        Message src = new Message();
        src.setMethodName("getMapDistancesToDriver");

        MessageBody messageBody = new MessageBody();
        messageBody.getMap().put("orderStatus", orderStatus);
        messageBody.getMap().put("addressDriver", lineAddressDriver);

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (getMapDistanceToDriver): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("OrderNotFoundException")) {
            throw new InputDataWrongException(jsonResponse);
        }


        // json -> Object
        Message response = gson.fromJson(jsonResponse, Message.class);

        // json -> Object
        Map<String, Object> mapOfOrders = response.getMessageBody().getMap();
        Map<Integer, Order> distances = new HashMap<>();

        if (mapOfOrders != null) {
            for (String key : mapOfOrders.keySet()) {
                Message concreteMessage = gson.fromJson(mapOfOrders.get(key) + "", Message.class);
                Order concreteOrder = getOrderFromMessage(concreteMessage.getMessageBody().getMap());
                distances.put(Integer.parseInt(key), concreteOrder);
            }
        }

        return distances;
    }

    @Override
    public User getUser(String accessToken) {

        Message src = new Message();
        src.setMethodName("getUser");

        MessageBody messageBody = new MessageBody();
        messageBody.getMap().put("accessToken", accessToken);

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (getUser): " + jsonResponse);
        Message response = gson.fromJson(jsonResponse, Message.class);

        return getUserFromMessage(response.getMessageBody().getMap());
    }

    @Override
    public List<Order> getAllOrdersUser(String accessToken) {

        Message src = new Message();
        src.setMethodName("getAllOrdersUser");

        MessageBody messageBody = new MessageBody();
        messageBody.getMap().put("accessToken", accessToken);

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (getAllUserOrders): " + jsonResponse);
        Message response = gson.fromJson(jsonResponse, Message.class);

        // json -> Object
        Map<String, Object> mapOfOrders = response.getMessageBody().getMap();
        List<Order> orders = new ArrayList<>();

        if (mapOfOrders != null) {
            for (String key : mapOfOrders.keySet()) {
                Message concreteMessage = gson.fromJson(mapOfOrders.get(key) + "", Message.class);
                orders.add(getOrderFromMessage(concreteMessage.getMessageBody().getMap()));
            }
        }

        return orders;
    }

    @Override
    public User updateUser(Map<String, String> map, String accessToken) throws RegisterException {

        Message src = new Message();
        src.setMethodName("updateUser");

        MessageBody messageBody = new MessageBody();
        for (String key : map.keySet()) {
            messageBody.getMap().put(key, map.get(key));
        }
        messageBody.getMap().put("accessToken", accessToken);

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (updateUser): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("RegisterException")) {
            throw new RegisterException(jsonResponse);
        }

        Message response = gson.fromJson(jsonResponse, Message.class);

        return getUserFromMessage(response.getMessageBody().getMap());
    }

    @Override
    public User deleteUser(String accessToken) throws WrongStatusOrderException {

        Message src = new Message();
        src.setMethodName("deleteUser");

        MessageBody messageBody = new MessageBody();
        messageBody.getMap().put("accessToken", accessToken);

        src.setMessageBody(messageBody);
        String jsonMessage = gson.toJson(src);

        pw.println(jsonMessage);
        pw.flush();

        String jsonResponse =  null;
        try {
            jsonResponse = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("SERVER OUT ---> CLIENT TAKE (deleteUser): " + jsonResponse);

        if (jsonResponse == null) {
            // ??
        } else if (jsonResponse.contains("WrongStatusOrderException")) {
            throw new WrongStatusOrderException(jsonResponse);
        }

        Message response = gson.fromJson(jsonResponse, Message.class);

        return getUserFromMessage(response.getMessageBody().getMap());
    }

    //additional methods for client
    public User getUserFromMessage(Map<String, Object> map) {

        //create User
        User user = new User(
                Enum.valueOf(UserIdentifier.class, map.get("identifier").toString()),
                map.get("phone").toString(),
                map.get("name").toString()
        );

        user.setId(Integer.parseInt(map.get("id").toString()));

        if (map.get("pass") != null) {
            user.setPass(map.get("pass").toString());
        }

        if (map.get("homeAddress") != null) {
            user.setHomeAddress(gson.fromJson(map.get("homeAddress").toString(), Address.class));
        }

        if (map.get("car") != null) {
            user.setCar(gson.fromJson(map.get("car").toString(), Car.class));
        }

        //get long list from line string with split ","
        if (map.get("orderIds") != null) {
            String line = map.get("orderIds").toString();

            List<Long> orderIds = new ArrayList<>();

            if (line.contains(",")) {
                String[] stringIds = line.split(",");
                for (int i = 0; i < stringIds.length; i++) {
                    orderIds.add(Long.parseLong(stringIds[i]));
                }

            } else if (!line.contains(",")) {
                orderIds.add(Long.parseLong(line));
            }

            user.setOrderIds(orderIds);
        }

        return user;
    }

    public Order getOrderFromMessage(Map<String, Object> map) {

        Order order = new Order();

        order.setId(Long.parseLong(map.get("id").toString()));
        order.setOrderStatus(Enum.valueOf(OrderStatus.class, map.get("orderStatus").toString()));
        order.setFrom(gson.fromJson(map.get("addressFrom").toString(), Address.class));
        order.setTo(gson.fromJson(map.get("addressTo").toString(), Address.class));

        Message messagePassenger = gson.fromJson(map.get("passenger").toString(), Message.class);
        order.setPassenger(getUserFromMessage(messagePassenger.getMessageBody().getMap()));

        if (map.get("driver") != null) {
            Message messageDriver = gson.fromJson(map.get("driver").toString(), Message.class);
            order.setDriver(getUserFromMessage(messageDriver.getMessageBody().getMap()));
        }

        order.setDistance(Integer.parseInt(map.get("distance").toString()));
        order.setPrice(Integer.parseInt(map.get("price").toString()));
        order.setMessage(map.get("message").toString());

        return order;
    }
}