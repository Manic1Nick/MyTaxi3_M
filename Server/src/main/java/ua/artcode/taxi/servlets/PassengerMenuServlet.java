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
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/user-info"})
public class PassengerMenuServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(PassengerMenuServlet.class);

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //validation

        req.getRequestDispatcher("/WEB-INF/pages/user-info.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");
        String accessToken = req.getParameter("accessToken");

        try {
            User found = userService.getUser(accessToken);

            HttpSession session = req.getSession(true);
            session.setAttribute("inSystem", true);
            session.setAttribute("accessToken", accessToken);
            session.setAttribute("currentUserName", found.getName());

            req.setAttribute("user", found);

            req.getRequestDispatcher("/WEB-INF/pages/" + action +".jsp").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("errorTitle", "Login Error");
            req.setAttribute("errorMessage", "invalid name");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }
        // redirect
    }

}
