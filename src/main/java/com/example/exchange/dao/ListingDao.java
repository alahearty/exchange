package com.example.exchange.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.exchange.entity.ListingEntity;
import com.example.exchange.helper.Response;
import com.example.exchange.helper.ResponseT;
import com.example.exchange.model.CategoryModel;
import com.example.exchange.model.InterestModel;
import com.example.exchange.model.InterestStatus;
import com.example.exchange.model.ListingModel;

public class ListingDao extends BaseDao {
    public ResponseT<List<ListingModel>> getListings() {
        String query = "SELECT l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name as categoryName, COUNT(i.id) as offers FROM listing l INNER JOIN Category c ON l.categoryId = c.id LEFT JOIN interest i ON l.id = i.offereelistingid AND i.offereestatus = 'pending' WHERE l.visible = 'true' GROUP BY l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name;";
        Map<Integer, Object> parameters = new HashMap<>();

        return findMany(query, parameters, rs -> {
            try {
                return new ListingModel(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl"),
                        rs.getInt("userId"),
                        new CategoryModel(rs.getInt("categoryId"), rs.getString("categoryName")),
                        rs.getInt("offers"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public ResponseT<List<ListingModel>> getListingsByUserId(int userId) {
        String query = "SELECT l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name as categoryName, COUNT(i.id) as offers FROM listing l INNER JOIN Category c ON l.categoryId = c.id LEFT JOIN interest i ON l.id = i.offereelistingid AND i.offereestatus = 'pending' WHERE l.userId = ? AND l.visible = 'true' GROUP BY l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name;";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, userId);

        return findMany(query, parameters, rs -> {
            try {
                return new ListingModel(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl"),
                        rs.getInt("userId"),
                        new CategoryModel(rs.getInt("categoryId"), rs.getString("categoryName")),
                        rs.getInt("offers"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public ResponseT<List<InterestModel>> getListingInterestModelByUserId(int userId) {
        String query = "SELECT l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name as categoryName, i.id as interestId, i.offerorquantity, i.offereequantity, i.offereestatus, i.offerorlistingid, i.offereelistingid FROM listing l INNER JOIN Category c ON l.categoryId = c.id INNER JOIN interest i ON l.id = i.offerorlistingid WHERE l.userId = ?";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, userId);

        return findMany(query, parameters, rs -> {
            try {
                ListingModel offerorListing = new ListingModel(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl"),
                        rs.getInt("userId"),
                        new CategoryModel(rs.getInt("categoryId"), rs.getString("categoryName")),
                        0);

                ListingModel offereeListing = getListingModelWithoutOffers(rs.getInt("offereelistingid")).getData();

                return new InterestModel(rs.getInt("interestId"),
                        rs.getInt("offerorquantity"),
                        rs.getInt("offereequantity"),
                        rs.getString("offereestatus"),
                        offerorListing,
                        offereeListing);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public ResponseT<List<ListingModel>> getListingsExceptAuthenticatedUser(int userId) {
        String query = "SELECT l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name as categoryName, COUNT(i.id) as offers FROM listing l INNER JOIN Category c ON l.categoryId = c.id LEFT JOIN interest i ON l.id = i.offereelistingid AND i.offereestatus = 'pending' WHERE l.userId != ? AND l.visible = 'true' GROUP BY l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name;";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, userId);

        return findMany(query, parameters, rs -> {
            try {
                return new ListingModel(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl"),
                        rs.getInt("userId"),
                        new CategoryModel(rs.getInt("categoryId"), rs.getString("categoryName")),
                        rs.getInt("offers"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public ResponseT<ListingModel> getListingModel(int id) {
        String query = "SELECT l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name as categoryName, COUNT(i.id) as offers FROM listing l INNER JOIN Category c ON l.categoryId = c.id LEFT JOIN interest i ON l.id = i.offereelistingid AND i.offereestatus = 'pending' WHERE l.id = ? AND l.visible = 'true' GROUP BY l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name;";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, id);

        return findOne(query, parameters, rs -> {
            try {
                return new ListingModel(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl"),
                        rs.getInt("userId"),
                        new CategoryModel(rs.getInt("categoryId"), rs.getString("categoryName")),
                        rs.getInt("offers"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public ResponseT<ListingEntity> getListing(int id) {
        String query = "SELECT * FROM listing WHERE id = ?";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, id);

        return findOne(query, parameters, rs -> {
            try {
                return new ListingEntity(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl"),
                        rs.getBoolean("visible"),
                        rs.getInt("userId"),
                        rs.getInt("categoryId"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public ResponseT<ListingModel> getListingModelWithoutOffers(int id) {
        String query = "SELECT l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name as categoryName FROM listing l INNER JOIN Category c ON l.categoryId = c.id WHERE l.id = ?";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, id);

        return findOne(query, parameters, rs -> {
            try {
                return new ListingModel(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl"),
                        rs.getInt("userId"),
                        new CategoryModel(rs.getInt("categoryId"), rs.getString("categoryName")),
                        0);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public ResponseT<List<ListingModel>> getListingsByCategoryId(int categoryId) {
        String query = "SELECT l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name as categoryName, COUNT(i.id) as offers FROM listing l INNER JOIN Category c ON l.categoryId = c.id LEFT JOIN interest i ON l.id = i.offereelistingid AND i.offereestatus = 'pending' WHERE l.categoryId = ? AND l.visible = 'true' GROUP BY l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name;";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, categoryId);

        return findMany(query, parameters, rs -> {
            try {
                return new ListingModel(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl"),
                        rs.getInt("userId"),
                        new CategoryModel(rs.getInt("categoryId"), rs.getString("categoryName")),
                        rs.getInt("offers"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public ResponseT<List<ListingModel>> getListingsByCategoryName(String categoryName) {
        String query = "SELECT l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name as categoryName, COUNT(i.id) as offers FROM listing l INNER JOIN Category c ON l.categoryId = c.id LEFT JOIN interest i ON l.id = i.offereelistingid AND i.offereestatus = 'pending' WHERE LOWER(c.name) = ? AND l.visible = 'true' GROUP BY l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name;";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, categoryName);

        return findMany(query, parameters, rs -> {
            try {
                return new ListingModel(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl"),
                        rs.getInt("userId"),
                        new CategoryModel(rs.getInt("categoryId"), rs.getString("categoryName")),
                        rs.getInt("offers"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public ResponseT<List<ListingModel>> getListingsByCategoryNameExceptAuthenticatedUser(String categoryName,
            int userId) {
        String query = "SELECT l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name as categoryName, COUNT(i.id) as offers FROM listing l INNER JOIN Category c ON l.categoryId = c.id LEFT JOIN interest i ON l.id = i.offereelistingid AND i.offereestatus = 'pending' WHERE LOWER(c.name) = ? AND l.userId != ? GROUP BY l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name;";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, categoryName);
        parameters.put(2, userId);

        return findMany(query, parameters, rs -> {
            try {
                return new ListingModel(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl"),
                        rs.getInt("userId"),
                        new CategoryModel(rs.getInt("categoryId"), rs.getString("categoryName")),
                        rs.getInt("offers"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public ResponseT<List<CategoryModel>> getCategories() {
        String query = "SELECT * FROM Category";
        Map<Integer, Object> parameters = new HashMap<>();

        return findMany(query, parameters, rs -> {
            try {
                return new CategoryModel(rs.getInt("id"),
                        rs.getString("name"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public Response makeOffer(String offerorListingId, String oferrorQuantity,
            String offereeListingId, String offereeQuantity) {
        List<String> errors = new ArrayList<>();
        if (offerorListingId == null || offerorListingId.isEmpty())
            errors.add("* Offeror listing id required");
        if (offereeListingId == null || offereeListingId.isEmpty())
            errors.add("* Offeree listing id required");
        if (oferrorQuantity == null || oferrorQuantity.isEmpty())
            errors.add("* The offeror quantity field is required");
        if (offereeQuantity == null || offereeQuantity.isEmpty())
            errors.add("* The offeree quantity field is required");

        int offerorquantity = 0;
        int offereequantity = 0;

        try {
            offerorquantity = Integer.parseInt(oferrorQuantity);
            offereequantity = Integer.parseInt(offereeQuantity);
        } catch (Exception e) {
            errors.add("* The offeror quantity field must be a number");
            errors.add("* The offeree quantity field must be a number");
        }

        if (offerorquantity <= 0)
            errors.add("* The offeror quantity field must be greater than 0.");
        if (offereequantity <= 0)
            errors.add("* The offeree quantity field must be greater than 0.");

        if (!errors.isEmpty())
            return Response.Fail(String.join("\n", errors));

        ListingEntity offerorListing = getListing(Integer.parseInt(offerorListingId)).getData();
        ListingEntity offereeListing = getListing(Integer.parseInt(offereeListingId)).getData();

        errors.clear();
        if (offerorquantity > offerorListing.getQuantity())
            errors.add("* The offeror quantity must not be greater than the offerror listing quantity.");
        if (offereequantity > offereeListing.getQuantity())
            errors.add("* The offeree quantity must not be greater than the offerree listing quantity.");

        if (!errors.isEmpty())
            return Response.Fail(String.join("\n", errors));

        String insertQuery = "INSERT INTO interest (offerorquantity, offereequantity, offereestatus, offerorlistingid, offereelistingid) VALUES (?, ?, ?, ?, ?)";
        Map<Integer, Object> insertValues = new HashMap<>();
        insertValues.put(1, offerorquantity);
        insertValues.put(2, offereequantity);
        insertValues.put(3, InterestStatus.PENDING);
        insertValues.put(4, offerorListing.getId());
        insertValues.put(5, offereeListing.getId());

        return write(insertQuery, insertValues);
    }
}
