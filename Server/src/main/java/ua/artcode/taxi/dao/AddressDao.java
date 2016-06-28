package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by serhii on 26.06.16.
 */
public class AddressDao implements GenericDao<Address> {

    @Override
    public Address create(Address el) {

        try (Connection connection = ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format
                    ("INSERT INTO addresses(country, city, street, house_num) VALUES ('%s', '%s', '%s', '%s')",
                            el.getCountry(),
                            el.getCity(),
                            el.getStreet(),
                            el.getHouseNum());
            statement.executeQuery(sqlSelect);

            ResultSet resultSet2 = statement.executeQuery
                    ("SELECT id FROM addresses s ORDER BY id DESC LIMIT 1;");
            resultSet2.next();
            el.setId(resultSet2.getLong("id"));

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
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
                    ("SELECT * FROM addresses WHERE id=%d", id);
            ResultSet resultSet = statement.executeQuery(sqlSelect);

            address = new Address(
                    resultSet.getString("country"),
                    resultSet.getString("city"),
                    resultSet.getString("street"),
                    resultSet.getString("house_num"));
            address.setId(resultSet.getLong("id"));

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
        return null;
    }

    @Override
    public Address getLast() {
        return null;
    }
}
