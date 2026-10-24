package com.example.exchange.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.exchange.dao.ListingDao;
import com.example.exchange.entity.ListingEntity;
import com.example.exchange.helper.Response;
import com.example.exchange.helper.ResponseT;
import com.example.exchange.model.ListingModel;
import com.example.exchange.model.UserModel;

@WebServlet(name = "ListingServlet", urlPatterns = { "/listing/create", "/listing/edit", "/listing/delete",
        "/listing/view" })
public class ListingServlet extends HttpServlet {
    private final ListingDao listingDao = new ListingDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        UserModel user = currentUser(req);

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        switch (path) {
            case "/listing/view":
                view(req, resp, user);
                break;
            case "/listing/create":
                showCreateForm(req, resp);
                break;
            case "/listing/edit":
                showEditForm(req, resp, user);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();
        UserModel user = currentUser(req);

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        switch (path) {
            case "/listing/create":
                create(req, resp, user);
                break;
            case "/listing/edit":
                update(req, resp, user);
                break;
            case "/listing/delete":
                delete(req, resp, user);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void view(HttpServletRequest req, HttpServletResponse resp, UserModel user)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"), -1);
        if (id <= 0) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        ResponseT<ListingModel> result = listingDao.getListingModelForOwner(id);
        if (!result.getSucceeded() || result.getData() == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        ListingModel listing = result.getData();
        if (listing.getUserId() != user.getId()) {
            // Non-owners may only see visible listings.
            ResponseT<ListingModel> visible = listingDao.getListingModel(id);
            if (!visible.getSucceeded() || visible.getData() == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            listing = visible.getData();
        }

        req.setAttribute("listing", listing);
        req.setAttribute("isOwner", listing.getUserId() == user.getId());
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/listing-detail.jsp");
        dispatcher.forward(req, resp);
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("categories", listingDao.getCategories().getData());
        req.setAttribute("mode", "create");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/listing-form.jsp");
        dispatcher.forward(req, resp);
    }

    private void create(HttpServletRequest req, HttpServletResponse resp, UserModel user)
            throws ServletException, IOException {
        ListingEntity listing = fromRequest(req, user.getId(), true, 0);
        ResponseT<Integer> result = listingDao.createListing(listing);

        if (result.getSucceeded()) {
            resp.sendRedirect(req.getContextPath() + "/listing/view?id=" + result.getData() + "&message=created");
            return;
        }

        req.setAttribute("error", result.getErrorMessage());
        req.setAttribute("listing", listing);
        req.setAttribute("categories", listingDao.getCategories().getData());
        req.setAttribute("mode", "create");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/listing-form.jsp");
        dispatcher.forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp, UserModel user)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"), -1);
        ResponseT<ListingEntity> result = listingDao.getListing(id);
        if (!result.getSucceeded() || result.getData() == null || result.getData().getUserId() != user.getId()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        req.setAttribute("listing", result.getData());
        req.setAttribute("categories", listingDao.getCategories().getData());
        req.setAttribute("mode", "edit");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/listing-form.jsp");
        dispatcher.forward(req, resp);
    }

    private void update(HttpServletRequest req, HttpServletResponse resp, UserModel user)
            throws ServletException, IOException {
        int id = parseInt(req.getParameter("id"), -1);
        ResponseT<ListingEntity> existing = listingDao.getListing(id);
        if (!existing.getSucceeded() || existing.getData() == null || existing.getData().getUserId() != user.getId()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        ListingEntity listing = fromRequest(req, user.getId(), existing.getData().isVisible(), id);
        Response result = listingDao.updateListing(listing);
        if (result.getSucceeded()) {
            resp.sendRedirect(req.getContextPath() + "/listing/view?id=" + id + "&message=updated");
            return;
        }

        req.setAttribute("error", result.getErrorMessage());
        req.setAttribute("listing", listing);
        req.setAttribute("categories", listingDao.getCategories().getData());
        req.setAttribute("mode", "edit");
        RequestDispatcher dispatcher = req.getRequestDispatcher("/jsp/listing-form.jsp");
        dispatcher.forward(req, resp);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp, UserModel user) throws IOException {
        int id = parseInt(req.getParameter("id"), -1);
        Response result = listingDao.softDeleteListing(id, user.getId());
        if (result.getSucceeded()) {
            resp.sendRedirect(req.getContextPath() + "/home?message=listing-removed");
        } else {
            resp.sendRedirect(req.getContextPath() + "/listing/view?id=" + id + "&error=delete-failed");
        }
    }

    private ListingEntity fromRequest(HttpServletRequest req, int userId, boolean visible, int id) {
        return new ListingEntity(
                id,
                req.getParameter("name"),
                req.getParameter("description"),
                req.getParameter("condition"),
                parseInt(req.getParameter("quantity"), 0),
                req.getParameter("imageUrl"),
                visible,
                userId,
                parseInt(req.getParameter("categoryId"), 0));
    }

    private UserModel currentUser(HttpServletRequest req) {
        Object value = req.getAttribute("authenticateduser");
        return value instanceof UserModel ? (UserModel) value : null;
    }

    private int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return fallback;
        }
    }
}
