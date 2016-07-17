package ua.artcode.taxi.servlets.orderServlets;

import org.apache.log4j.Logger;
import ua.artcode.taxi.exception.DriverOrderActionException;
import ua.artcode.taxi.exception.OrderNotFoundException;
import ua.artcode.taxi.exception.WrongStatusOrderException;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/order/done"})
public class OrderDoneServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(OrderDoneServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String orderId = req.getParameter("id");

        try {
            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

            Order order = userService.closeOrder(accessToken, Integer.parseInt(orderId));
            User user = userService.getUser(accessToken);

            req.setAttribute("order", order);
            req.setAttribute("user", user);

            req.getRequestDispatcher("/WEB-INF/pages/order-info.jsp").forward(req, resp);

        } catch (OrderNotFoundException e) {
            LOG.error(e);
            req.setAttribute("errorTitle", "Order not found in data base");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        } catch (WrongStatusOrderException e) {
            LOG.error(e);
            req.setAttribute("errorTitle", "This order has wrong status (not IN_PROGRESS)");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        } catch (DriverOrderActionException e) {
            LOG.error(e);
            req.setAttribute("errorTitle", "Order not found in driver orders list");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }
    }

}