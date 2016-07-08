package ua.artcode.taxi.service;

import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.exception.RegisterException;
import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.model.UserIdentifier;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface ValidatorHibernate {

    boolean validateLogin(String phone, String password, EntityManagerFactory entityManagerFactory) throws Exception;
    boolean validateRegistration(String phone, EntityManagerFactory entityManagerFactory) throws RegisterException;
    boolean validateAddress(Address address, EntityManagerFactory entityManagerFactory) throws InputDataWrongException;
    boolean validateChangeRegistration(UserIdentifier identifier, int id, String phone, EntityManagerFactory entityManagerFactory) throws RegisterException;
}
