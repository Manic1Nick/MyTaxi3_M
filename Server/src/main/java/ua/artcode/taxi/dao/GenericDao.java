package ua.artcode.taxi.dao;

import java.util.List;

/**
 * Created by serhii on 26.06.16.
 */

public interface GenericDao<T> {

    // return entity with id
    T create(T el);

    boolean delete(T el);

    T findById(int id);

    List<T> getAll(int offset, int length);

    // use el id for find in db
    T update(T el);

    T getLast();

}
