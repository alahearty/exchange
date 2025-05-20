package com.example.exchange.controller;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.exchange.dao.UserDao;
import com.example.exchange.helper.Response;
import com.example.exchange.model.RegistrationModel;

@WebServlet(name = "RegisterServlet", urlPatterns = { "/register" })
public class RegisterServlet extends HttpServlet {
    UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nextJSP = "/jsp/register.jsp";
        RequestDispatcher dispatcher = req.getRequestDispatcher(nextJSP);
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        RegistrationModel registration = new RegistrationModel(req.getParameter("firstname"),
                req.getParameter("lastname"),
                req.getParameter("email"),
                req.getParameter("password"),
                req.getParameter("confirmpassword"));

        Response result = userDao.register(registration);

        if (result.getSucceeded()) {
            req.setAttribute("message", MessageFormat.format("Registered user with email: ''{0}'' successfully", registration.getEmail()));
        } else {
            registration.setPassword(null);
            registration.setConfirmPassword(null);
            req.setAttribute("registration", registration);
            req.setAttribute("error", result.getErrorMessage());
        }
        
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/register.jsp");
        dispatcher.forward(req, resp);
    }
}
