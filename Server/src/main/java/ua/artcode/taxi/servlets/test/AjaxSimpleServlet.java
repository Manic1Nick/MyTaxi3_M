package ua.artcode.taxi.servlets.test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by serhii on 17.07.16.
 */
@WebServlet(urlPatterns = {"/ajax/hello"})
public class AjaxSimpleServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");

        String responseMessage = "Hello " + name + ", Server Time " + LocalTime.now().toString();

        resp.getWriter().print(responseMessage);
    }
}
