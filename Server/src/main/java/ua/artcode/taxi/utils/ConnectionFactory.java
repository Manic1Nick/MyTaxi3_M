package ua.artcode.taxi.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionFactory {

    private static String url;
    private static String user;
    private static String password;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // load from properties
        url = "jdbc:mysql://localhost:3306/mytaxi";
        user = "root";
        password = "hp6930p";
    }

    public static Connection createConnection() throws SQLException {

        return DriverManager.getConnection(
                url, user, password);
    }


    public static EntityManager createEntityManager() {

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("myunit");

        return entityManagerFactory.createEntityManager();
    }
}
