package com.example.exchange.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "OfferServlet", urlPatterns = { "/offer" })
public class OfferServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();

        try {
            switch (action) {
                case "/offer":
                    offer(req, resp);
                    break;
                case "/accept":
                    accept(req, resp);
                    break;
                case "/reject":
                    reject(req, resp);
                    break;
                default:
                    listOffer(req, resp);
                    break;
            }
        } catch (SQLException ex) {
        }
    }

    private void offer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookList.jsp");
        dispatcher.forward(request, response);
    }

    private void accept(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookList.jsp");
        dispatcher.forward(request, response);
    }

    private void reject(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookList.jsp");
        dispatcher.forward(request, response);
    }

    private void listOffer(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookList.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    }
}
