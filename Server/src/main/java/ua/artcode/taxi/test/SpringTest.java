package ua.artcode.taxi.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.artcode.taxi.service.UserService;

/**
 * Created by Iurii on 16.07.2016.
 */
public class SpringTest {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");

        UserService myService = (UserService) context.getBean("userService");
        String result = myService.login("093","test");

        System.out.println("Access key for user  " + myService.getUser(result).getName() + " is " + result);
    }
}
