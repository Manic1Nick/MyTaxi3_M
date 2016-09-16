package ua.artcode.taxi.soap.endpoint;

import ua.artcode.taxi.exception.RegisterException;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.Map;

/**
 * Created by Iurii on 22.07.2016.
 */

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface UserEndpoint  {

    @WebMethod
    String login(String phone, String pass) throws java.lang.Exception;

    @WebMethod
    User getUser(String accessToken);
}
