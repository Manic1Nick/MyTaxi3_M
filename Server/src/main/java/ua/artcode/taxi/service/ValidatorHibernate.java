package ua.artcode.taxi.service;

import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.exception.RegisterException;
import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.model.UserIdentifier;

import javax.persistence.EntityManager;

public interface ValidatorHibernate {

    boolean validateLogin(String phone, String password, EntityManager manager) throws Exception;
    boolean validateRegistration(String phone, EntityManager manager) throws RegisterException;
    boolean validateAddress(Address address, EntityManager manager) throws InputDataWrongException;
    boolean validateChangeRegistration(UserIdentifier identifier, int id, String phone, EntityManager manager) throws RegisterException;
}
