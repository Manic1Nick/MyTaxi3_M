package ua.artcode.taxi.service;

import ua.artcode.taxi.dao.UserDaoHibernate;
import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.exception.RegisterException;
import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.model.UserIdentifier;

import javax.persistence.EntityManager;
import java.util.List;

public class ValidatorHibernateImpl implements ValidatorHibernate {

    private UserDaoHibernate userDaoHibernate;

    public ValidatorHibernateImpl(UserDaoHibernate userDaoHibernate) {
        this.userDaoHibernate = userDaoHibernate;
    }

    @Override
    public boolean validateLogin(String phone, String password, EntityManager manager) {
        boolean result = false;

        List<String> phones = userDaoHibernate.getAllRegisteredPhones(manager);

        for (String s : phones) {
            if (phone.equals(s)) {
                String foundPass = userDaoHibernate.findByPhone(phone, manager).getPass();

                if(password.equals(foundPass)){
                    result = true;
                }
            }
        }

        return result;
    }

    @Override
    public boolean validateRegistration(String phone, EntityManager manager) throws RegisterException {

        List<String> phones = userDaoHibernate.getAllRegisteredPhones(manager);

        for (String s : phones) {
            if (phone.equals(s)) {
                throw new RegisterException("This phone using already");
            }
        }

        return true;
    }

    @Override
    public boolean validateAddress(Address address, EntityManager manager) throws InputDataWrongException {

        if (address.getCountry().equals("")) {
            throw new InputDataWrongException("Wrong data: country");
            //result = false;
        } else if (address.getCity().equals("")) {
            throw new InputDataWrongException("Wrong data: city");
            //result = false;
        } else if (address.getStreet().equals("")) {
            throw new InputDataWrongException("Wrong data: street");
            //result = false;
        } else if (address.getHouseNum().equals("")) {
            throw new InputDataWrongException("Wrong data: houseNum");
            //result = false;
        }

        return true;
    }

    @Override
    public boolean validateChangeRegistration(UserIdentifier identifier, int id, String phone, EntityManager manager)
            throws RegisterException {

        List<String> phones = userDaoHibernate.getAllRegisteredPhones(manager);

        for (String s : phones) {
            if (phone.equals(s)) {
                User foundUser = userDaoHibernate.findByPhone(phone , manager);
                if (id != foundUser.getId() && !identifier.equals(foundUser.getIdentifier())) {
                    throw new RegisterException("This phone using already");
                }
            }
        }

        return true;
    }
}
