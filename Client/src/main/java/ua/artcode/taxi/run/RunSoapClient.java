package ua.artcode.taxi.run;

import ua.artcode.taxi.soap.endpoint.Exception_Exception;
import ua.artcode.taxi.soap.endpoint.UserEndpoint;
import ua.artcode.taxi.soap.endpoint.UserEndpointImplService;

/**
 * Created by Iurii on 22.07.2016.
 */
public class RunSoapClient {

    public static void main(String[] args) throws Exception_Exception {
        UserEndpoint userEndpoint = new UserEndpointImplService().getUserEndpointImplPort();

        String key = userEndpoint.login("093","test");

        System.out.println(key);
    }
}
