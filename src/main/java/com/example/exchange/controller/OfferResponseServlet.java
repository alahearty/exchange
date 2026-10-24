package com.example.exchange.controller;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.exchange.dao.ListingDao;
import com.example.exchange.helper.Response;
import com.example.exchange.model.UserModel;

@WebServlet(name = "OfferResponseServlet", urlPatterns = { "/offer/respond" })
public class OfferResponseServlet extends HttpServlet {
    private final ListingDao listingDao = new ListingDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/my-offers");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        Object value = req.getAttribute("authenticateduser");
        UserModel user = value instanceof UserModel ? (UserModel) value : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int interestId = parseInt(req.getParameter("interestId"), -1);
        String action = req.getParameter("action");
        if (interestId <= 0 || (!"accept".equalsIgnoreCase(action) && !"reject".equalsIgnoreCase(action))) {
            session.setAttribute("flashError", "Invalid offer response request.");
            resp.sendRedirect(req.getContextPath() + "/my-offers");
            return;
        }

        Response result = listingDao.respondToOffer(interestId, user.getId(), "accept".equalsIgnoreCase(action));
        if (result.getSucceeded()) {
            session.setAttribute("flashSuccess",
                    "accept".equalsIgnoreCase(action) ? "Offer accepted. Both listings are now closed."
                            : "Offer rejected.");
        } else {
            session.setAttribute("flashError", result.getErrorMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/my-offers");
    }

    private int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return fallback;
        }
    }
}
