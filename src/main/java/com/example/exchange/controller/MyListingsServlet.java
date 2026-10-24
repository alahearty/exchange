package com.example.exchange.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.exchange.dao.ListingDao;
import com.example.exchange.model.UserModel;

@WebServlet(name = "MyListingsServlet", urlPatterns = { "/listing/mine" })
public class MyListingsServlet extends HttpServlet {
    private final ListingDao listingDao = new ListingDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object value = req.getAttribute("authenticateduser");
        if (!(value instanceof UserModel)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        UserModel user = (UserModel) value;
        req.setAttribute("listings", listingDao.getAllListingsByUserId(user.getId()).getData());
        req.setAttribute("navActive", "mine");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/my-listings.jsp");
        dispatcher.forward(req, resp);
    }
}
