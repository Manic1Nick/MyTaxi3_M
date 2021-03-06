package ua.artcode.taxi.servlets.notAjax;

import org.apache.log4j.Logger;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.utils.BeansFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/order/all"})
public class GetAllOrdersServlet extends HttpServlet {

    private UserService userService;
    private static final Logger LOG = Logger.getLogger(GetAllOrdersServlet.class);

    @Override
    public void init() throws ServletException {
        userService = BeansFactory.createUserService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       /* try {
            String accessToken = String.valueOf(req.getSession().getAttribute("accessToken"));

            User found = userService.getUser(accessToken);

            Map<Integer, Order> distanceMap = userService.getMapDistancesToDriver(
                    OrderStatus.IN_PROGRESS.toString(),
                    found.getUserCurrentLocation());

            Object[] objArray = distanceMap.keySet().toArray();
            int[] distances = new int[objArray.length];
            for (int i = 0; i < objArray.length; i++) {
                distances[i] = (int) objArray[i];
            }
            Arrays.sort(distances);

            Order[] orders = new Order[distances.length];
            int[] distancesKm = new int[distances.length];

            for (int i = 0; i < distanceMap.size(); i++) {
                orders[i] = distanceMap.get(distances[i]);
                distancesKm[i] = distances[i]/1000;
                orders[i].setDistanceToDriver(distancesKm[i]);
            }

            req.setAttribute("orders", orders);

            req.getRequestDispatcher("/WEB-INF/pages/order-find.jsp").forward(req, resp);

        } catch (InputDataWrongException e) {
            LOG.error(e);
            req.setAttribute("error", "Wrong calculation in Google API");
            req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
        }*/
    }

}
