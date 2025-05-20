package com.example.exchange.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.exchange.dao.ListingDao;
import com.example.exchange.helper.ResponseT;
import com.example.exchange.model.InterestModel;
import com.example.exchange.model.UserModel;

@WebServlet(name = "MyOffersServlet", urlPatterns = { "/my-offers" })
public class MyOffersServlet extends HttpServlet {
    private static ListingDao listingDao = new ListingDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object authUser = req.getAttribute("authenticateduser");
        UserModel user = (UserModel) authUser;
        ResponseT<List<InterestModel>> interests = listingDao.getListingInterestModelByUserId(user.getId());
        req.setAttribute("interests", interests.getData());
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/my-offers.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    }
}
