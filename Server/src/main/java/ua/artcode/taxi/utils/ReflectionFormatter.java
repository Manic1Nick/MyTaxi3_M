package ua.artcode.taxi.utils;

import com.google.gson.Gson;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.to.Message;
import ua.artcode.taxi.to.MessageBody;

import java.util.HashMap;
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
}