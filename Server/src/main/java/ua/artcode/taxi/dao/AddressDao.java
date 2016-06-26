package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Address;

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

        return null;
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
