package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AddressDao implements GenericDao<Address> {

    @Override
    public Address create(Address el) {

        long id = getId(el);

        if (id > 0) {
            el.setId(id);

        } else if (id < 0) {
            try (Connection connection = ConnectionFactory.createConnection();
                 Statement statement = connection.createStatement();) {

                connection.setAutoCommit(false);

                String sqlInsert = String.format
                        ("INSERT INTO addresses(country, city, street, house_num) VALUES ('%s', '%s', '%s', '%s');",
                                el.getCountry(),
                                el.getCity(),
                                el.getStreet(),
                                el.getHouseNum());
                statement.execute(sqlInsert);

                ResultSet resultSet = statement.executeQuery
                        ("SELECT id FROM addresses s ORDER BY id DESC LIMIT 1;");
                resultSet.next();
                el.setId(resultSet.getLong("id"));

                connection.commit();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return el;
    }

    @Override
    public boolean delete(Address el) {
        return false;
    }

    @Override
    public Address findById(long id) {

        Address address = null;

        try (Connection connection =
                     ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format
                    ("SELECT * FROM addresses WHERE id=%d;", id);
            ResultSet resultSet = statement.executeQuery(sqlSelect);

            while (resultSet.next()) {
                address = new Address(
                        resultSet.getString("country"),
                        resultSet.getString("city"),
                        resultSet.getString("street"),
                        resultSet.getString("house_num"));
                address.setId(id);
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return address;
    }

    @Override
    public List<Address> getAll(int offset, int length) {
        return null;
    }

    @Override
    public Address update(Address el) {

        return create(el);
    }

    @Override
    public Address getLast() {
        return null;
    }

    @Override
    public long getId(Address el) {

        long id = -1;

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            ResultSet resultSet = statement.executeQuery(String.format
                    ("SELECT id FROM addresses WHERE country='%s' AND city='%s' AND street='%s' AND house_num='%s' LIMIT 1;",
                            el.getCountry(),
                            el.getCity(),
                            el.getStreet(),
                            el.getHouseNum()));

            if (resultSet.next()) {
                id = resultSet.getLong("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }
}
