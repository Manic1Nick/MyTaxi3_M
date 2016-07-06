package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Car;
import ua.artcode.taxi.utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CarDao implements GenericDao<Car> {

    @Override
    public Car create(Car el) {

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            ResultSet resultSet1 = statement.executeQuery(String.format
                    ("Select id from cars where type='%s' and model='%s' and number='%s' limit 1;",
                            el.getType(),
                            el.getModel(),
                            el.getNumber()));

            if (resultSet1.next()) {
                el.setId(resultSet1.getLong("id"));
            }
            else {

                String sqlInsert = String.format
                        ("INSERT INTO cars(type, model, number) VALUES ('%s', '%s', '%s');",
                                el.getType(),
                                el.getModel(),
                                el.getNumber());
                statement.execute(sqlInsert);

                ResultSet resultSet = statement.executeQuery
                        ("SELECT id FROM cars s ORDER BY id DESC LIMIT 1;");
                resultSet.next();
                el.setId(resultSet.getLong("id"));

                connection.commit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return el;
    }

    @Override
    public boolean delete(Car el) {
        Connection connection = null;
        boolean resultSet = false;
        try {
            connection = ConnectionFactory.createConnection();
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);
            resultSet = statement.execute(String.format("delete from cars where type=%d;", el.getId()));
            connection.commit();
            }
             catch (SQLException e) {
                e.printStackTrace();
            }
            return resultSet;
    }

    @Override
    public Car findById(long id) {

        Car car = null;

        try (Connection connection =
                     ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format
                    ("SELECT * FROM cars WHERE id=%d;", id);
            ResultSet resultSet = statement.executeQuery(sqlSelect);

            while (resultSet.next()) {
                car = new Car(
                        resultSet.getString("type"),
                        resultSet.getString("model"),
                        resultSet.getString("number"));
                car.setId(resultSet.getLong("id"));
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return car;
    }

    @Override
    public List<Car> getAll(int offset, int length) {
        return null;
    }

    @Override
    public Car update(Car el) {
        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlUpdate = String.format
                    ("UPDATE cars SET type='%s', model='%s', number='%s' WHERE id=%d;",
                            el.getType(),
                            el.getModel(),
                            el.getNumber(),
                            el.getId());
            statement.executeQuery(sqlUpdate);

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return el;
    }

    @Override
    public Car getLast() {
        return null;
    }
}
