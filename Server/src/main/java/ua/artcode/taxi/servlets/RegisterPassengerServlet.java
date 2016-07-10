package ua.artcode.taxi.servlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.exception.RegisterException;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/register-passenger"})
public class RegisterPassengerServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(RegisterPassengerServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //validation

        req.getRequestDispatcher("/WEB-INF/pages/register-passenger.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, String> registerData = new HashMap<>();

        registerData.put("phone", req.getParameter("phone"));
        registerData.put("pass", req.getParameter("pass"));
        registerData.put("name", req.getParameter("name"));
        registerData.put("homeAddress",
                req.getParameter("country") + " "
                + req.getParameter("city") + " "
                + req.getParameter("street") + " "
                + req.getParameter("houseNum"));

        try {
            User user = userService.registerPassenger(registerData);

            req.setAttribute("user", user);
            resp.sendRedirect("index.jsp");

        } catch (RegisterException e) {
            LOG.error(e);
            req.setAttribute("errorTitle", "Register Error");
            req.setAttribute("errorMessage", "invalid data");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }

    }
}


