package ua.artcode.taxi.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitSpringContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /*String springLocation = sce.getServletContext().getInitParameter("springLocation");
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext(springLocation);

        sce.getServletContext().setAttribute("spring-context",applicationContext);*/
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
