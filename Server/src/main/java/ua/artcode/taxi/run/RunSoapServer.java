package ua.artcode.taxi.run;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.soap.endpoint.UserEndpointImpl;

import javax.xml.ws.Endpoint;

/**
 * Created by Iurii on 22.07.2016.
 */
public class RunSoapServer {



    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        UserService userService = (UserService) context.getBean("userService");

        Endpoint.publish("http://localhost:9999/soap/user", new UserEndpointImpl(userService));

    }

}
