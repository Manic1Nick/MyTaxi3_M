package ua.artcode.taxi.dao;

import ua.artcode.taxi.model.Car;

import java.util.List;

/**
 * Created by ivan on 26.06.16.
 */
public class CarDao  implements GenericDao{
    @Override
    public Object create(Object el) {
        return null;
    }

    @Override
    public boolean delete(Object el) {
        return false;
    }

    @Override
    public Car findById(int id) {
        return null;
    }

    @Override
    public List getAll(int offset, int length) {
        return null;
    }

    @Override
    public Object update(Object el) {
        return null;
    }

    @Override
    public Object getLast() {
        return null;
    }
}
