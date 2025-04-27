package org.example;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CalculatorServlet", urlPatterns = "/calculate")
public class CalculatorServlet extends HttpServlet {
    private final CalculatorService service = new CalculatorService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String op = req.getParameter("op");
        int a = Integer.parseInt(req.getParameter("a"));
        int b = Integer.parseInt(req.getParameter("b"));

        int result;
        switch (op) {
            case "+" -> result = service.add(a, b);
            case "-" -> result = service.subtract(a, b);
            case "*" -> result = service.multiply(a, b);
            case "/" -> result = service.divide(a, b);
            default  -> throw new ServletException("Unknown op: " + op);
        }

        System.out.println("CALC_LOG: " + a + " " + op + " " + b + " = " + result);

        req.setAttribute("result", result);
        req.getRequestDispatcher("/index.jsp")
                .forward(req, resp);
    }
}
