package ua.artcode.taxi.servlet;

import org.apache.log4j.Logger;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.service.UserServiceJdbcImpl;
import ua.artcode.taxi.service.ValidatorJdbcImpl;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// web.xml
@WebServlet(urlPatterns = {"/login"})
public class RegisterServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(RegisterServlet.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // validation,
        req.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String phone = req.getParameter("phone");
        String pass = req.getParameter("pass");

        try {
            String accessKey = userService.login(phone,pass);

            User user = userService.getUser(accessKey);

            req.setAttribute("user", user);

            req.getRequestDispatcher("/WEB-INF/pages/user-info.jsp").forward(req,resp);

        } catch (Exception e) {
            LOG.error(e);

            req.setAttribute("error", e);
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req,resp);
        }


    }
}
