package ua.artcode.taxi.soap.endpoint;

import ua.artcode.taxi.exception.RegisterException;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;

import javax.jws.WebService;
import java.util.Map;


@WebService(endpointInterface = "ua.artcode.taxi.soap.endpoint.UserEndpoint")
public class UserEndpointImpl implements UserEndpoint {

    private UserService userService;

    public UserEndpointImpl() {
    }

    public UserEndpointImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String login(String phone, String pass) throws java.lang.Exception {
        return userService.login(phone, pass);
    }

    @Override
    public User getUser(String accessToken) {
        return userService.getUser(accessToken);
    }
}
