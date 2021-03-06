package ua.artcode.taxi.servlets.userServlets;

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

@WebServlet(urlPatterns = {"/ajax/user-info"})
public class AjaxGetUserInfoServlet extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(AjaxGetUserInfoServlet.class);

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

        User found = userService.getUser(accessToken);

        req.setAttribute("user", found);

        req.getServletContext().getRequestDispatcher("/WEB-INF/pages/ajax-user-info.jsp").include(req, resp);
    }

}
