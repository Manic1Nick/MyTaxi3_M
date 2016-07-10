package ua.artcode.taxi.servlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/make-order"})
public class MakeOrderServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(MakeOrderServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //validation

        req.getRequestDispatcher("/WEB-INF/pages/make-order.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*String accessToken = String.valueOf(req.getAttribute("accessToken"));*/
        String lineFrom = req.getParameter("countryFrom") + " " +
                        req.getParameter("cityFrom") + " " +
                        req.getParameter("streetFrom") + " " +
                        req.getParameter("houseNumFrom");
        String lineTo = req.getParameter("countryTo") + " " +
                        req.getParameter("cityTo") + " " +
                        req.getParameter("streetTo") + " " +
                        req.getParameter("houseNumTo");
        String message = req.getParameter("message");

        try {
            HttpSession session = req.getSession(true);
            String accessToken = String.valueOf(session.getAttribute("accessToken"));

            Order order = userService.makeOrder(accessToken, lineFrom, lineTo, message);

            req.setAttribute("order", order);
            req.getRequestDispatcher("/WEB-INF/pages/order-info.jsp").forward(req, resp);

        } catch (Exception e) {
            LOG.error(e);
            req.setAttribute("error", e);
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }

    }
}
