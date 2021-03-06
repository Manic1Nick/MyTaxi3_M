package ua.artcode.taxi.servlets.userServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.model.Address;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/ajax/user/get-location"})
public class AjaxGetUserLocationServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AjaxGetUserLocationServlet.class);

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Address userLocation = userService.getUserLocation();
            String location = userLocation.getCountry() + "," +
                    userLocation.getCity() + "," +
                    userLocation.getStreet() + "," +
                    userLocation.getHouseNum();

            resp.getWriter().write(location);

        } catch (Exception e) {
            LOG.error(e);
            resp.getWriter().write("User not found");
        }
    }

}
