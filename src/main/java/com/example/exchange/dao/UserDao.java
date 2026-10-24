package com.example.exchange.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.exchange.helper.PasswordHasher;
import com.example.exchange.helper.Response;
import com.example.exchange.helper.ResponseT;
import com.example.exchange.model.RegistrationModel;
import com.example.exchange.model.UserModel;

public class UserDao extends BaseDao {
    private static final class LoginRecord {
        private final UserModel user;
        private final String passwordHash;

        private LoginRecord(UserModel user, String passwordHash) {
            this.user = user;
            this.passwordHash = passwordHash;
        }
    }

    public ResponseT<UserModel> login(String email, String password) {
        List<String> errors = new ArrayList<>();
        if (email == null || email.isEmpty())
            errors.add("* The email field is required");
        if (password == null || password.isEmpty())
            errors.add("* The password field is required");

        if (!errors.isEmpty())
            return Response.FailT(String.join("\n", errors));

        String query = "SELECT id, email, firstname, lastname, password FROM \"user\" WHERE LOWER(email)=?";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, email.trim().toLowerCase());

        ResponseT<LoginRecord> userResponse = findOne(query, parameters, rs -> {
            try {
                UserModel user = new UserModel(rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("firstname"),
                        rs.getString("lastname"));
                return new LoginRecord(user, rs.getString("password"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });

        if (!userResponse.getSucceeded())
            return Response.FailT(userResponse.getErrorMessage());

        LoginRecord record = userResponse.getData();
        if (record == null || !PasswordHasher.matches(password, record.passwordHash))
            return Response.FailT("The username and password do not match");

        // One-time upgrade for imported/demo data that still contains a plaintext value.
        if (!PasswordHasher.isHash(record.passwordHash)) {
            updatePassword(record.user.getId(), PasswordHasher.hash(password));
        }

        return Response.SuccessT(record.user);
    }

    public Response register(RegistrationModel registration) {
        List<String> errors = new ArrayList<>();
        if (registration.getFirstName() == null || registration.getFirstName().isEmpty()) errors.add("* The first name field is required");
        if (registration.getLastName() == null || registration.getLastName().isEmpty()) errors.add("* The last name field is required");
        if (registration.getEmail() == null || registration.getEmail().isEmpty()) errors.add("* The email field is required");
        if (registration.getPassword() == null || registration.getPassword().isEmpty()) errors.add("* The password field is required");
        if (registration.getConfirmPassword() == null || registration.getConfirmPassword().isEmpty()) errors.add("* The confirm password field is required");

        if (registration.getPassword() != null && registration.getConfirmPassword() != null && !registration.getPassword().equals(registration.getConfirmPassword())) errors.add("* The password and confirm password must match.");

        if (!errors.isEmpty()) return Response.Fail(String.join("\n", errors));

        String selectUserQuery = "SELECT id FROM \"user\" WHERE LOWER(email)=?";
        Map<Integer, Object> selectUserParameters = new HashMap<>();
        selectUserParameters.put(1, registration.getEmail().trim().toLowerCase());

        ResponseT<Integer> getUserResponse = findOne(selectUserQuery, selectUserParameters, rs -> {
            try {
                return rs.getInt("id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        });

        if (getUserResponse.getSucceeded() && getUserResponse.getData() != null) return Response.Fail(MessageFormat.format("User with email: ''{0}'' already exist", registration.getEmail()));
        if (!getUserResponse.getSucceeded()) return Response.Fail(getUserResponse.getErrorMessage());

        String insertUserQuery = "INSERT INTO \"user\" (firstname, lastname, email, password) VALUES (?, ?, ?, ?)";
        Map<Integer, Object> insertUserValues = new HashMap<>();
        insertUserValues.put(1, registration.getFirstName().trim());
        insertUserValues.put(2, registration.getLastName().trim());
        insertUserValues.put(3, registration.getEmail().trim());
        insertUserValues.put(4, PasswordHasher.hash(registration.getPassword()));

        return write(insertUserQuery, insertUserValues);
    }

    private void updatePassword(int userId, String passwordHash) {
        String query = "UPDATE \"user\" SET password = ? WHERE id = ?";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, passwordHash);
        parameters.put(2, userId);
        write(query, parameters);
    }

    /**
     * Converts legacy plaintext rows to BCrypt hashes. The method is idempotent and must be
     * called before serving login traffic. It never logs or returns passwords.
     *
     * @return number of migrated rows, or -1 when the migration could not run
     */
    public int migrateLegacyPasswords() {
        String select = "SELECT id, password FROM \"user\"";
        String update = "UPDATE \"user\" SET password = ? WHERE id = ?";
        int migrated = 0;

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Integer> ids = new ArrayList<>();
                List<String> hashes = new ArrayList<>();

                try (PreparedStatement statement = connection.prepareStatement(select);
                        ResultSet result = statement.executeQuery()) {
                    while (result.next()) {
                        String stored = result.getString("password");
                        if (!PasswordHasher.isHash(stored)) {
                            ids.add(result.getInt("id"));
                            hashes.add(PasswordHasher.hash(stored == null ? "" : stored));
                        }
                    }
                }

                if (!ids.isEmpty()) {
                    try (PreparedStatement statement = connection.prepareStatement(update)) {
                        for (int i = 0; i < ids.size(); i++) {
                            statement.setString(1, hashes.get(i));
                            statement.setInt(2, ids.get(i));
                            statement.addBatch();
                        }
                        statement.executeBatch();
                        migrated = ids.size();
                    }
                }

                connection.commit();
                return migrated;
            } catch (Exception ex) {
                connection.rollback();
                throw ex;
            }
        } catch (Exception ex) {
            return -1;
        }
    }
}
