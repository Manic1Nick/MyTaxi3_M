package ua.artcode.taxi.service;

import ua.artcode.taxi.dao.UserDao;
import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.exception.RegisterException;
import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.model.UserIdentifier;

import java.util.List;

public class ValidatorJdbcImpl implements Validator {

    private UserDao userDao;

    public ValidatorJdbcImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean validateLogin(String phone, String password) {
        boolean result = false;

        List<String> phones = userDao.getAllRegisteredPhones();

        for (String s : phones) {
            if (phone.equals(s)) {
                String foundPass = userDao.findByPhone(phone).getPass();

                if(password.equals(foundPass)){
                    result = true;
                }
            }
        }

        return result;
    }

    @Override
    public boolean validateRegistration(String phone) throws RegisterException {

        List<String> phones = userDao.getAllRegisteredPhones();

        for (String s : phones) {
            if (phone.equals(s)) {
                throw new RegisterException("This phone using already");
            }
        }

        return true;
    }

    @Override
    public boolean validateAddress(Address address) throws InputDataWrongException {

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
    public boolean validateChangeRegistration(UserIdentifier identifier, int id, String phone)
            throws RegisterException {

        List<String> phones = userDao.getAllRegisteredPhones();

        for (String s : phones) {
            if (phone.equals(s)) {
                User foundUser = userDao.findByPhone(phone);
                if (id != foundUser.getId() && !identifier.equals(foundUser.getIdentifier())) {
                    throw new RegisterException("This phone using already");
                }
            }
        }

        return true;
    }
}
