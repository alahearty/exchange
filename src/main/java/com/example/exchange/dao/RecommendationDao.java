package com.example.exchange.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.exchange.helper.ResponseT;
import com.example.exchange.model.ListingModel;

/**
 * DAO for recommendation-related queries.
 * Fetches user preferences and candidate items for recommendations.
 */
public class RecommendationDao extends BaseDao {

    /**
     * Get categories the user has shown interest in (through offers made).
     * Returns category names the user has targeted when making offers.
     */
    public ResponseT<List<String>> getPreferredCategoriesByUserId(int userId) {
        String query = "SELECT DISTINCT LOWER(c.name) as categoryName " +
                "FROM interest i " +
                "INNER JOIN listing l ON i.offereelistingid = l.id " +
                "INNER JOIN Category c ON l.categoryId = c.id " +
                "WHERE i.offerorlistingid IN (SELECT id FROM listing WHERE userId = ?)";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, userId);

        return findMany(query, params, rs -> {
            try {
                return rs.getString("categoryName");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * Get category pairs from all offers (for association rule mining).
     * Returns [offerorCategory, offereeCategory] pairs.
     */
    public ResponseT<List<String[]>> getCategoryPairsFromOffers() {
        String query = "SELECT co.name as offerorCat, ce.name as offereeCat " +
                "FROM interest i " +
                "INNER JOIN listing lo ON i.offerorlistingid = lo.id " +
                "INNER JOIN listing le ON i.offereelistingid = le.id " +
                "INNER JOIN Category co ON lo.categoryId = co.id " +
                "INNER JOIN Category ce ON le.categoryId = ce.id";
        Map<Integer, Object> params = new HashMap<>();

        return findMany(query, params, rs -> {
            try {
                return new String[]{rs.getString("offerorCat"), rs.getString("offereeCat")};
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * Get listings by category names, excluding user's own, with optional limit.
     */
    public ResponseT<List<ListingModel>> getListingsByCategoriesExceptUser(
            List<String> categoryNames, int excludeUserId, int limit) {
        if (categoryNames == null || categoryNames.isEmpty()) {
            return new ResponseT<>(false, null, "No categories");
        }

        String placeholders = categoryNames.stream()
                .map(c -> "?")
                .collect(Collectors.joining(", "));
        String query = "SELECT l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name as categoryName, COUNT(i.id) as offers " +
                "FROM listing l " +
                "INNER JOIN Category c ON l.categoryId = c.id " +
                "LEFT JOIN interest i ON l.id = i.offereelistingid AND i.offereestatus = 'pending' " +
                "WHERE l.userId != ? AND l.visible = 'true' AND LOWER(c.name) IN (" + placeholders + ") " +
                "GROUP BY l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name " +
                "ORDER BY offers DESC LIMIT ?";

        Map<Integer, Object> params = new HashMap<>();
        params.put(1, excludeUserId);
        int idx = 2;
        for (String cat : categoryNames) {
            params.put(idx++, cat.toLowerCase());
        }
        params.put(idx, limit);

        return findMany(query, params, rs -> {
            try {
                return new ListingModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl"),
                        rs.getInt("userId"),
                        new com.example.exchange.model.CategoryModel(rs.getInt("categoryId"), rs.getString("categoryName")),
                        rs.getInt("offers"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * Get popular listings (most offers) excluding user, with limit.
     */
    public ResponseT<List<ListingModel>> getPopularListingsExceptUser(int excludeUserId, int limit) {
        String query = "SELECT l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name as categoryName, COUNT(i.id) as offers " +
                "FROM listing l " +
                "INNER JOIN Category c ON l.categoryId = c.id " +
                "LEFT JOIN interest i ON l.id = i.offereelistingid AND i.offereestatus = 'pending' " +
                "WHERE l.userId != ? AND l.visible = 'true' " +
                "GROUP BY l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name " +
                "ORDER BY offers DESC, l.id LIMIT ?";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, excludeUserId);
        params.put(2, limit);

        return findMany(query, params, rs -> {
            try {
                return new ListingModel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl"),
                        rs.getInt("userId"),
                        new com.example.exchange.model.CategoryModel(rs.getInt("categoryId"), rs.getString("categoryName")),
                        rs.getInt("offers"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
