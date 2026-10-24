package com.example.exchange.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.exchange.dao.ListingDao;
import com.example.exchange.entity.ListingEntity;
import com.example.exchange.helper.Response;
import com.example.exchange.service.RecommendationService;
import com.example.exchange.helper.ResponseT;
import com.example.exchange.model.HomeListingModel;
import com.example.exchange.model.ListingModel;
import com.example.exchange.model.UserModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet(name = "HomeServlet", urlPatterns = { "/home" })
public class HomeServlet extends HttpServlet {
    private static ListingDao listingDao = new ListingDao();
    private static RecommendationService recommendationService = new RecommendationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String listingId = req.getParameter("interest");
        if (listingId != null) {
            makeOffer(req, resp, listingId);
        } else {
            listListings(req, resp);
        }
    }

    private void makeOffer(HttpServletRequest request, HttpServletResponse response, String listingId)
            throws IOException, ServletException {
        Object authUser = request.getAttribute("authenticateduser");
        if (authUser != null) {
            UserModel user = (UserModel) authUser;
            int id = 0;
            try {
                id = Integer.parseInt(listingId);
            } catch (Exception e) {
                request.setAttribute("error", "Invalid listing");
            }

            ObjectMapper mapper = new ObjectMapper();
            ResponseT<List<ListingModel>> offeror = listingDao.getListingsByUserId(user.getId());

            if (!offeror.getSucceeded()) {
                request.setAttribute("error", offeror.getErrorMessage());
            } else {
                Map<String, String> data = offeror.getData().stream()
                        .collect(Collectors.toMap(x -> x.getName(), x -> {
                            try {
                                return mapper.writeValueAsString(x);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                            return "";
                        }));
                request.setAttribute("offeror", data);
            }

            ResponseT<ListingModel> offeree = listingDao.getListingModelWithoutOffers(id);
            if (!offeree.getSucceeded()) {
                request.setAttribute("error", offeree.getErrorMessage());
            } else {
                request.setAttribute("offeree", offeree.getData());
            }

            request.setAttribute("interest", listingId);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/make-offer.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("login");
        }
    }

    private void listListings(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String category = request.getParameter("category") == null ? "all"
                : request.getParameter("category").toLowerCase();

        List<ListingModel> listings = null;

        Object authUser = request.getAttribute("authenticateduser");
        if (authUser != null) {
            UserModel user = (UserModel) authUser;
            listings = "all".equals(category) ? listingDao.getListingsExceptAuthenticatedUser(user.getId()).getData()
                    : listingDao.getListingsByCategoryNameExceptAuthenticatedUser(category, user.getId()).getData();
        } else {
            listings = "all".equals(category) ? listingDao.getListings().getData()
                    : listingDao.getListingsByCategoryName(category).getData();
        }

        HomeListingModel model = new HomeListingModel(category, listingDao.getCategories().getData(), listings);
        request.setAttribute("listing", model);
        request.setAttribute("active", category);

        Integer recUserId = authUser != null ? ((UserModel) authUser).getId() : null;
        request.setAttribute("recommendations", recommendationService.getRecommendations(recUserId));

        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/home.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException, NumberFormatException {
        req.setCharacterEncoding("UTF-8");
        Object authUser = req.getAttribute("authenticateduser");
        if (!(authUser instanceof UserModel)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        UserModel user = (UserModel) authUser;

        String offereeListingId = req.getParameter("interest");
        String offerorListingId = req.getParameter("offerorlistingid");
        String offerorQuantity = req.getParameter("offerorquantity");
        String offereeQuantity = req.getParameter("offereequantity");

        try {
            int offereeId = Integer.parseInt(offereeListingId);
            int offerorId = Integer.parseInt(offerorListingId);

            ResponseT<ListingEntity> offeror = listingDao.getListing(offerorId);
            ResponseT<ListingEntity> offeree = listingDao.getListing(offereeId);
            if (offeror.getData() == null || offeror.getData().getUserId() != user.getId()) {
                req.setAttribute("error", "You can only offer one of your own listings.");
            } else if (!offeror.getData().isVisible()) {
                req.setAttribute("error", "Your selected listing is no longer available.");
            } else if (offeree.getData() == null || !offeree.getData().isVisible()) {
                req.setAttribute("error", "The requested listing no longer exists.");
            } else if (offeree.getData().getUserId() == user.getId()) {
                req.setAttribute("error", "You cannot make an offer on your own listing.");
            } else {
                Response result = listingDao.makeOffer(offerorListingId, offerorQuantity, offereeListingId,
                        offereeQuantity);
                if (result.getSucceeded()) {
                    req.getSession().setAttribute("flashSuccess", "Offer submitted successfully.");
                    resp.sendRedirect(req.getContextPath() + "/my-offers");
                    return;
                }
                req.setAttribute("error", result.getErrorMessage());
            }
        } catch (Exception ex) {
            req.setAttribute("error", "Please select a listing and valid quantities.");
        }

        req.setAttribute("interest", offereeListingId);
        makeOffer(req, resp, offereeListingId == null ? "" : offereeListingId);
    }
}
