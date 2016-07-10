package ua.artcode.taxi.servlets;

import org.apache.log4j.Logger;
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

@WebServlet(urlPatterns = {"/register-driver"})
public class RegisterDriverServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(RegisterDriverServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //validation

        req.getRequestDispatcher("/WEB-INF/pages/register-driver.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, String> registerData = new HashMap<>();

        registerData.put("phone", req.getParameter("phone"));
        registerData.put("pass", req.getParameter("pass"));
        registerData.put("name", req.getParameter("name"));
        registerData.put("carType", req.getParameter("carType"));
        registerData.put("carModel", req.getParameter("carModel"));
        registerData.put("carNumber", req.getParameter("carNumber"));

        try {
            User user = userService.registerDriver(registerData);

            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/pages/user-info.jsp").forward(req, resp);

        } catch (Exception e) {
            LOG.error(e);
            req.setAttribute("error", e);
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }

    }
}


