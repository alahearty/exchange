package com.example.exchange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import com.example.exchange.dao.ListingDao;
import com.example.exchange.dao.UserDao;
import com.example.exchange.entity.ListingEntity;
import com.example.exchange.helper.PasswordHasher;
import com.example.exchange.helper.Response;
import com.example.exchange.helper.ResponseT;
import com.example.exchange.model.InterestStatus;
import com.example.exchange.model.RegistrationModel;
import com.example.exchange.model.UserModel;

public class ExchangeWorkflowTest {
    private final UserDao userDao = new UserDao();
    private final ListingDao listingDao = new ListingDao();

    private Connection connection() throws Exception {
        String url = System.getenv().getOrDefault("EXCHANGE_DB_URL", "jdbc:postgresql://localhost:5432/exchange_test");
        String user = System.getenv().getOrDefault("EXCHANGE_DB_USER", "worinium");
        String password = System.getenv().getOrDefault("EXCHANGE_DB_PASSWORD", "worisoft@123");
        return DriverManager.getConnection(url, user, password);
    }

    private void execute(Statement statement, String sql) throws Exception {
        statement.execute(sql);
    }

    @Before
    public void resetDatabase() throws Exception {
        try (Connection connection = connection(); Statement statement = connection.createStatement()) {
            execute(statement, "DROP TABLE IF EXISTS interest");
            execute(statement, "DROP TABLE IF EXISTS listing");
            execute(statement, "DROP TABLE IF EXISTS category");
            execute(statement, "DROP TABLE IF EXISTS \"user\"");
            execute(statement,
                    "CREATE TABLE \"user\" (id SERIAL PRIMARY KEY, email VARCHAR(100) UNIQUE NOT NULL, firstname VARCHAR(100) NOT NULL, lastname VARCHAR(100) NOT NULL, password TEXT NOT NULL)");
            execute(statement,
                    "CREATE TABLE category (id SERIAL PRIMARY KEY, name VARCHAR(200) UNIQUE NOT NULL)");
            execute(statement,
                    "CREATE TABLE listing (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, description TEXT NOT NULL, condition VARCHAR(100) NOT NULL, quantity INT NOT NULL, imageurl TEXT NOT NULL, visible BOOLEAN, userid INT NOT NULL REFERENCES \"user\"(id), categoryid INT NOT NULL REFERENCES category(id))");
            execute(statement,
                    "CREATE TABLE interest (id SERIAL PRIMARY KEY, offerorquantity INT NOT NULL, offereequantity INT NOT NULL, offereestatus VARCHAR(100) NOT NULL, offerorlistingid INT NOT NULL REFERENCES listing(id), offereelistingid INT NOT NULL REFERENCES listing(id))");
            execute(statement, "INSERT INTO category (name) VALUES ('Electronics'), ('Furniture')");
        }
    }

    private int register(String email, String first, String last) {
        Response response = userDao.register(new RegistrationModel(first, last, email, "Password1!", "Password1!"));
        assertTrue(response.getSucceeded());
        ResponseT<UserModel> login = userDao.login(email, "Password1!");
        assertTrue(login.getSucceeded());
        return login.getData().getId();
    }

    private int createListing(int userId, String name) {
        ResponseT<Integer> created = listingDao.createListing(
                new ListingEntity(name, "Synthetic test item", "new", 1, "", true, userId, 1));
        assertTrue(created.getSucceeded());
        return created.getData();
    }

    private int offerId(int offereeListingId) throws Exception {
        try (Connection connection = connection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT id FROM interest WHERE offereelistingid = ? ORDER BY id DESC LIMIT 1")) {
            statement.setInt(1, offereeListingId);
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                return result.getInt(1);
            }
        }
    }

    private String storedPassword(String email) throws Exception {
        try (Connection connection = connection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT password FROM \"user\" WHERE email = ?")) {
            statement.setString(1, email);
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                return result.getString(1);
            }
        }
    }

    @Test
    public void registrationAndLoginAreValidated() throws Exception {
        int userId = register("seller@test.local", "Test", "Seller");
        assertTrue(userId > 0);

        String stored = storedPassword("seller@test.local");
        assertTrue(PasswordHasher.isHash(stored));
        assertFalse("Password1!".equals(stored));
        assertFalse(stored.contains("Password1!"));

        Response duplicate = userDao.register(
                new RegistrationModel("Test", "Seller", "seller@test.local", "Password1!", "Password1!"));
        assertFalse(duplicate.getSucceeded());

        Response mismatch = userDao.register(
                new RegistrationModel("Bad", "Password", "bad@test.local", "Password1!", "Password2!"));
        assertFalse(mismatch.getSucceeded());

        assertFalse(userDao.login("seller@test.local", "wrong").getSucceeded());
        assertTrue(userDao.login("seller@test.local", "Password1!").getSucceeded());
    }

    @Test
    public void listingCrudEnforcesOwnershipAndSoftDelete() {
        int sellerId = register("seller@test.local", "Test", "Seller");
        int otherId = register("other@test.local", "Other", "User");
        int listingId = createListing(sellerId, "Test Chair");

        ResponseT<ListingEntity> found = listingDao.getListing(listingId);
        assertTrue(found.getSucceeded());
        assertNotNull(found.getData());
        assertEquals("Test Chair", found.getData().getName());

        ListingEntity update = new ListingEntity(listingId, "Updated Chair", "Updated description", "used", 2, "",
                true, sellerId, 2);
        assertTrue(listingDao.updateListing(update).getSucceeded());
        assertEquals("Updated Chair", listingDao.getListing(listingId).getData().getName());

        ListingEntity unauthorized = new ListingEntity(listingId, "Hijacked", "Nope", "used", 2, "", true, otherId, 2);
        assertFalse(listingDao.updateListing(unauthorized).getSucceeded());
        assertEquals("Updated Chair", listingDao.getListing(listingId).getData().getName());

        assertFalse(listingDao.softDeleteListing(listingId, otherId).getSucceeded());
        assertTrue(listingDao.getListing(listingId).getData().isVisible());

        assertTrue(listingDao.softDeleteListing(listingId, sellerId).getSucceeded());
        assertFalse(listingDao.getListing(listingId).getData().isVisible());
        assertNull(listingDao.getListingModel(listingId).getData());
    }

    @Test
    public void offerCanOnlyBeAcceptedByOwnerAndOnlyOnce() throws Exception {
        int sellerId = register("seller@test.local", "Test", "Seller");
        int buyerId = register("buyer@test.local", "Test", "Buyer");
        int wantedListing = createListing(sellerId, "Wanted Tent");
        int offeredListing = createListing(buyerId, "Offered Lamp");

        Response offer = listingDao.makeOffer(String.valueOf(offeredListing), "1", String.valueOf(wantedListing), "1");
        assertTrue(offer.getSucceeded());
        assertFalse(listingDao.makeOffer(String.valueOf(offeredListing), "1", String.valueOf(wantedListing), "1")
                .getSucceeded());
        int interestId = offerId(wantedListing);

        assertFalse(listingDao.respondToOffer(interestId, buyerId, true).getSucceeded());
        assertTrue(listingDao.respondToOffer(interestId, sellerId, true).getSucceeded());

        try (Connection connection = connection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT offereestatus FROM interest WHERE id = ?")) {
            statement.setInt(1, interestId);
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                assertEquals(InterestStatus.ACCEPTED, result.getString(1));
            }
        }

        assertFalse(listingDao.getListing(wantedListing).getData().isVisible());
        assertFalse(listingDao.getListing(offeredListing).getData().isVisible());
        assertFalse(listingDao.respondToOffer(interestId, sellerId, true).getSucceeded());
    }

    @Test
    public void ownerCanRejectOffer() throws Exception {
        int sellerId = register("seller@test.local", "Test", "Seller");
        int buyerId = register("buyer@test.local", "Test", "Buyer");
        int wantedListing = createListing(sellerId, "Wanted Tent");
        int offeredListing = createListing(buyerId, "Offered Lamp");

        assertTrue(listingDao.makeOffer(String.valueOf(offeredListing), "1", String.valueOf(wantedListing), "1")
                .getSucceeded());
        int interestId = offerId(wantedListing);
        assertTrue(listingDao.respondToOffer(interestId, sellerId, false).getSucceeded());

        try (Connection connection = connection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT offereestatus FROM interest WHERE id = ?")) {
            statement.setInt(1, interestId);
            try (ResultSet result = statement.executeQuery()) {
                assertTrue(result.next());
                assertEquals(InterestStatus.REJECTED, result.getString(1));
            }
        }

        assertTrue(listingDao.getListing(wantedListing).getData().isVisible());
        assertFalse(listingDao.respondToOffer(interestId, sellerId, false).getSucceeded());
    }

    @Test
    public void legacyPlaintextPasswordIsMigratedAndStillWorks() throws Exception {
        try (Connection connection = connection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO \"user\" (firstname, lastname, email, password) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, "Legacy");
            statement.setString(2, "User");
            statement.setString(3, "legacy@test.local");
            statement.setString(4, "legacy-password");
            statement.executeUpdate();
        }

        assertTrue(userDao.migrateLegacyPasswords() >= 1);

        String stored = storedPassword("legacy@test.local");
        assertTrue(PasswordHasher.isHash(stored));
        assertFalse("legacy-password".equals(stored));
        assertTrue(userDao.login("legacy@test.local", "legacy-password").getSucceeded());
        assertFalse(userDao.login("legacy@test.local", "wrong-password").getSucceeded());
        assertEquals(0, userDao.migrateLegacyPasswords());
    }
}
