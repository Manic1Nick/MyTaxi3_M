package ua.artcode.taxi.utils;

import com.google.gson.Gson;
import ua.artcode.taxi.model.*;
import ua.artcode.taxi.to.Message;
import ua.artcode.taxi.to.MessageBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionFormatter {

    private Gson gson;

    public static Map<String, Object> userToJsonMap(User user) {

        Map<String, Object> map = new HashMap<>();

        map.put("id", user.getId() + "");
        map.put("identifier", user.getIdentifier().toString());
        map.put("phone", user.getPhone());
        map.put("pass", user.getPass());
        map.put("name", user.getName());
        map.put("homeAddress", new Gson().toJson(user.getHomeAddress()));
        map.put("car", new Gson().toJson(user.getCar()));

        //create string list of ids with split sigh ","
        if (user.getOrderIds().size() > 0) {
            List<Long> orderIds = new ArrayList<>(user.getOrderIds());
            String stringIds = "";
            for (int i = 0; i < orderIds.size(); i++) {
                stringIds += orderIds.get(i) + ",";
            }
            int indexEnd = orderIds.size() > 0 ? stringIds.length()-1 : stringIds.length() ;

            map.put("orderIds", stringIds.substring(0, indexEnd));
        }

        return map;
    }

    public static Map<String, Object> orderToJsonMap(Order order) {

        Map<String, Object> map = new HashMap<>();

        map.put("id", order.getId() + "");

        if (order.getOrderStatus() != null) {
            map.put("orderStatus", order.getOrderStatus().toString());
        }

        map.put("addressFrom", new Gson().toJson(order.getFrom()));
        map.put("addressTo", new Gson().toJson(order.getTo()));

        Message messagePassenger = new Message();
        messagePassenger.setMessageBody(new MessageBody(userToJsonMap(order.getPassenger())));
        map.put("passenger", new Gson().toJson(messagePassenger));

        if (order.getDriver() != null) {
            Message messageDriver = new Message();

            messageDriver.setMessageBody(new MessageBody(userToJsonMap(order.getDriver())));
            map.put("driver", new Gson().toJson(messageDriver));
        }

        map.put("distance", order.getDistance() + "");
        map.put("price", order.getPrice() + "");
        map.put("message", order.getMessage() + "");

        return map;
    }

    public User getUserFromMap(Map<String, Object> map) {

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

    public Order getOrderFromMap(Map<String, Object> map) {

        Order order = new Order();

        order.setId(Long.parseLong(map.get("id").toString()));
        order.setOrderStatus(Enum.valueOf(OrderStatus.class, map.get("orderStatus").toString()));
        order.setFrom(gson.fromJson(map.get("addressFrom").toString(), Address.class));
        order.setTo(gson.fromJson(map.get("addressTo").toString(), Address.class));

        Message messagePassenger = gson.fromJson(map.get("passenger").toString(), Message.class);
        order.setPassenger(getUserFromMap(messagePassenger.getMessageBody().getMap()));

        if (map.get("driver") != null) {
            Message messageDriver = gson.fromJson(map.get("driver").toString(), Message.class);
            order.setDriver(getUserFromMap(messageDriver.getMessageBody().getMap()));
        }

        order.setDistance(Integer.parseInt(map.get("distance").toString()));
        order.setPrice(Integer.parseInt(map.get("price").toString()));
        order.setMessage(map.get("message").toString());

        return order;
    }
}