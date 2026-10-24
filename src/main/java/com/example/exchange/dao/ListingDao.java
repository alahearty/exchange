package com.example.exchange.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public ResponseT<List<ListingModel>> getAllListingsByUserId(int userId) {
        String query = "SELECT l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, l.visible, c.name as categoryName, COUNT(i.id) as offers FROM listing l INNER JOIN Category c ON l.categoryId = c.id LEFT JOIN interest i ON l.id = i.offereelistingid AND i.offereestatus = 'pending' WHERE l.userId = ? GROUP BY l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, l.visible, c.name ORDER BY l.id DESC;";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, userId);

        return findMany(query, parameters, rs -> {
            try {
                ListingModel model = new ListingModel(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("condition"),
                        rs.getInt("quantity"),
                        rs.getString("imageUrl"),
                        rs.getInt("userId"),
                        new CategoryModel(rs.getInt("categoryId"), rs.getString("categoryName")),
                        rs.getInt("offers"));
                model.setVisible(rs.getBoolean("visible"));
                return model;
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
        if (offerorListing == null || offereeListing == null) {
            return Response.Fail("One of the selected listings no longer exists.");
        }
        if (!offerorListing.isVisible() || !offereeListing.isVisible()) {
            return Response.Fail("One of the selected listings is no longer available.");
        }
        if (offerorquantity > offerorListing.getQuantity())
            errors.add("* The offeror quantity must not be greater than the offerror listing quantity.");
        if (offereequantity > offereeListing.getQuantity())
            errors.add("* The offeree quantity must not be greater than the offerree listing quantity.");

        if (!errors.isEmpty())
            return Response.Fail(String.join("\n", errors));

        String duplicateQuery = "SELECT COUNT(*) AS offerCount FROM interest WHERE offerorlistingid = ? AND offereelistingid = ? AND offereestatus = 'pending'";
        Map<Integer, Object> duplicateValues = new HashMap<>();
        duplicateValues.put(1, offerorListing.getId());
        duplicateValues.put(2, offereeListing.getId());
        ResponseT<Integer> duplicate = findOne(duplicateQuery, duplicateValues, rs -> {
            try {
                return rs.getInt("offerCount");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        });
        if (duplicate.getSucceeded() && duplicate.getData() != null && duplicate.getData() > 0) {
            return Response.Fail("You already have a pending offer for this listing.");
        }

        String insertQuery = "INSERT INTO interest (offerorquantity, offereequantity, offereestatus, offerorlistingid, offereelistingid) VALUES (?, ?, ?, ?, ?)";
        Map<Integer, Object> insertValues = new HashMap<>();
        insertValues.put(1, offerorquantity);
        insertValues.put(2, offereequantity);
        insertValues.put(3, InterestStatus.PENDING);
        insertValues.put(4, offerorListing.getId());
        insertValues.put(5, offereeListing.getId());

        return write(insertQuery, insertValues);
    }

    public ResponseT<ListingModel> getListingModelForOwner(int id) {
        String query = "SELECT l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name as categoryName, COUNT(i.id) as offers FROM listing l INNER JOIN Category c ON l.categoryId = c.id LEFT JOIN interest i ON l.id = i.offereelistingid AND i.offereestatus = 'pending' WHERE l.id = ? GROUP BY l.id, l.name, l.description, l.condition, l.quantity, l.imageUrl, l.userId, l.categoryId, c.name;";
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

    public ResponseT<Integer> createListing(ListingEntity listing) {
        String error = validateListing(listing);
        if (error != null)
            return Response.FailT(error);

        String query = "INSERT INTO listing (name, description, condition, quantity, imageurl, visible, userid, categoryid) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, listing.getName().trim());
            statement.setString(2, listing.getDescription().trim());
            statement.setString(3, listing.getCondition().trim());
            statement.setInt(4, listing.getQuantity());
            statement.setString(5, listing.getImageUrl() == null ? "" : listing.getImageUrl().trim());
            statement.setBoolean(6, listing.isVisible());
            statement.setInt(7, listing.getUserId());
            statement.setInt(8, listing.getCategoryId());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next())
                    return Response.SuccessT(result.getInt(1));
            }

            return Response.FailT("Unable to create listing");
        } catch (Exception ex) {
            return Response.FailT(ex.getMessage());
        }
    }

    public Response updateListing(ListingEntity listing) {
        String error = validateListing(listing);
        if (error != null)
            return Response.Fail(error);

        String query = "UPDATE listing SET name = ?, description = ?, condition = ?, quantity = ?, imageurl = ?, categoryid = ?, visible = ? WHERE id = ? AND userid = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, listing.getName().trim());
            statement.setString(2, listing.getDescription().trim());
            statement.setString(3, listing.getCondition().trim());
            statement.setInt(4, listing.getQuantity());
            statement.setString(5, listing.getImageUrl() == null ? "" : listing.getImageUrl().trim());
            statement.setInt(6, listing.getCategoryId());
            statement.setBoolean(7, listing.isVisible());
            statement.setInt(8, listing.getId());
            statement.setInt(9, listing.getUserId());

            int updated = statement.executeUpdate();
            return updated == 0 ? Response.Fail("Listing not found or you do not own it") : Response.Success();
        } catch (Exception ex) {
            return Response.Fail(ex.getMessage());
        }
    }

    public Response softDeleteListing(int id, int userId) {
        String query = "UPDATE listing SET visible = false WHERE id = ? AND userid = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, userId);
            int updated = statement.executeUpdate();
            return updated == 0 ? Response.Fail("Listing not found or you do not own it")
                    : Response.Success();
        } catch (Exception ex) {
            return Response.Fail(ex.getMessage());
        }
    }

    public ResponseT<List<InterestModel>> getReceivedInterestsByUserId(int userId) {
        String query = "SELECT i.id AS interestId, i.offerorquantity, i.offereequantity, i.offereestatus, "
                + "ol.id AS offerorId, ol.name AS offerorName, ol.description AS offerorDescription, ol.condition AS offerorCondition, ol.quantity AS offerorQuantity, ol.imageurl AS offerorImageUrl, ol.userid AS offerorUserId, ol.categoryid AS offerorCategoryId, oc.name AS offerorCategoryName, "
                + "el.id AS offereeId, el.name AS offereeName, el.description AS offereeDescription, el.condition AS offereeCondition, el.quantity AS offereeQuantity, el.imageurl AS offereeImageUrl, el.userid AS offereeUserId, el.categoryid AS offereeCategoryId, ec.name AS offereeCategoryName "
                + "FROM interest i "
                + "INNER JOIN listing ol ON i.offerorlistingid = ol.id "
                + "INNER JOIN category oc ON ol.categoryid = oc.id "
                + "INNER JOIN listing el ON i.offereelistingid = el.id "
                + "INNER JOIN category ec ON el.categoryid = ec.id "
                + "WHERE el.userid = ? ORDER BY i.id DESC";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, userId);

        return findMany(query, parameters, rs -> {
            try {
                ListingModel offeror = new ListingModel(rs.getInt("offerorId"),
                        rs.getString("offerorName"),
                        rs.getString("offerorDescription"),
                        rs.getString("offerorCondition"),
                        rs.getInt("offerorQuantity"),
                        rs.getString("offerorImageUrl"),
                        rs.getInt("offerorUserId"),
                        new CategoryModel(rs.getInt("offerorCategoryId"), rs.getString("offerorCategoryName")),
                        0);

                ListingModel offeree = new ListingModel(rs.getInt("offereeId"),
                        rs.getString("offereeName"),
                        rs.getString("offereeDescription"),
                        rs.getString("offereeCondition"),
                        rs.getInt("offereeQuantity"),
                        rs.getString("offereeImageUrl"),
                        rs.getInt("offereeUserId"),
                        new CategoryModel(rs.getInt("offereeCategoryId"), rs.getString("offereeCategoryName")),
                        0);

                return new InterestModel(rs.getInt("interestId"),
                        rs.getInt("offerorquantity"),
                        rs.getInt("offereequantity"),
                        rs.getString("offereestatus"),
                        offeror,
                        offeree);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public Response respondToOffer(int interestId, int userId, boolean accept) {
        String select = "SELECT i.offereestatus, i.offerorlistingid, i.offereelistingid, l.userid AS ownerid, l.visible "
                + "FROM interest i INNER JOIN listing l ON i.offereelistingid = l.id "
                + "WHERE i.id = ? FOR UPDATE";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try {
                String status;
                int offerorListingId;
                int offereeListingId;
                int ownerId;
                boolean listingVisible;

                try (PreparedStatement statement = connection.prepareStatement(select)) {
                    statement.setInt(1, interestId);
                    try (ResultSet result = statement.executeQuery()) {
                        if (!result.next()) {
                            connection.rollback();
                            return Response.Fail("Offer not found");
                        }
                        status = result.getString("offereestatus");
                        offerorListingId = result.getInt("offerorlistingid");
                        offereeListingId = result.getInt("offereelistingid");
                        ownerId = result.getInt("ownerid");
                        listingVisible = result.getBoolean("visible");
                    }
                }

                if (ownerId != userId) {
                    connection.rollback();
                    return Response.Fail("You can only respond to offers on your own listings");
                }

                if (!InterestStatus.PENDING.equalsIgnoreCase(status)) {
                    connection.rollback();
                    return Response.Fail("This offer has already been " + status);
                }

                if (accept && !listingVisible) {
                    connection.rollback();
                    return Response.Fail("This listing is no longer available");
                }

                String newStatus = accept ? InterestStatus.ACCEPTED : InterestStatus.REJECTED;
                try (PreparedStatement statement = connection
                        .prepareStatement("UPDATE interest SET offereestatus = ? WHERE id = ?")) {
                    statement.setString(1, newStatus);
                    statement.setInt(2, interestId);
                    statement.executeUpdate();
                }

                if (accept) {
                    // A successful exchange closes the listing. Reject other pending
                    // offers for the same item in the same transaction.
                    try (PreparedStatement statement = connection.prepareStatement(
                            "UPDATE interest SET offereestatus = ? WHERE offereelistingid = ? AND id <> ? AND offereestatus = ?")) {
                        statement.setString(1, InterestStatus.REJECTED);
                        statement.setInt(2, offereeListingId);
                        statement.setInt(3, interestId);
                        statement.setString(4, InterestStatus.PENDING);
                        statement.executeUpdate();
                    }

                    try (PreparedStatement statement = connection.prepareStatement(
                            "UPDATE listing SET visible = false WHERE id = ? OR id = ?")) {
                        statement.setInt(1, offerorListingId);
                        statement.setInt(2, offereeListingId);
                        statement.executeUpdate();
                    }
                }

                connection.commit();
                return Response.Success();
            } catch (Exception ex) {
                connection.rollback();
                return Response.Fail(ex.getMessage());
            }
        } catch (Exception ex) {
            return Response.Fail(ex.getMessage());
        }
    }

    private String validateListing(ListingEntity listing) {
        List<String> errors = new ArrayList<>();
        if (listing == null) {
            return "* Listing is required";
        }
        if (listing.getName() == null || listing.getName().trim().isEmpty())
            errors.add("* The listing name is required");
        if (listing.getDescription() == null || listing.getDescription().trim().isEmpty())
            errors.add("* The description is required");
        if (listing.getCondition() == null || listing.getCondition().trim().isEmpty())
            errors.add("* The condition is required");
        if (listing.getQuantity() <= 0)
            errors.add("* The quantity must be greater than 0");
        if (listing.getCategoryId() <= 0)
            errors.add("* A category must be selected");

        return errors.isEmpty() ? null : String.join("\n", errors);
    }
}
