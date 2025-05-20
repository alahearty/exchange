package com.example.exchange.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.exchange.dao.UserDao;
import com.example.exchange.helper.ResponseT;
import com.example.exchange.model.UserModel;

@WebServlet(name = "LoginServlet", urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {
    UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String referer = (String) req.getAttribute("origin");
        req.setAttribute("origin", referer);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/login.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        ResponseT<UserModel> result = userDao.login(email, password);

        if (result.getSucceeded()) {
            HttpSession session = req.getSession();
            session.setAttribute("user", result.getData());
            // session.setMaxInactiveInterval(60);

            String url = req.getParameter("origin").isEmpty() ? "/exchange/home" : req.getParameter("origin");

            resp.sendRedirect(url);
        } else {
            req.setAttribute("error", result.getErrorMessage());
            RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/login.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
