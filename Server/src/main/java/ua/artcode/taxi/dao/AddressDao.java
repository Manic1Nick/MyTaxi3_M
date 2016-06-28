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
        return null;
    }

    @Override
    public boolean delete(Address el) {
        return false;
    }

    @Override
    public Address findById(int id) {

        Address address = null;

        try (Connection connection =
                     ConnectionFactory.createConnection();
             Statement statement = connection.createStatement();) {

            connection.setAutoCommit(false);

            String sqlSelect = String.format
                    ("SELECT country, city, street, house_num FROM addresses WHERE id=%d", id);
            ResultSet resultSet = statement.executeQuery(sqlSelect);

            address = new Address(
                    resultSet.getString("country"),
                    resultSet.getString("city"),
                    resultSet.getString("street"),
                    resultSet.getString("house_num"));

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
