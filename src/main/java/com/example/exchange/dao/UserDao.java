package com.example.exchange.dao;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.exchange.helper.Response;
import com.example.exchange.helper.ResponseT;
import com.example.exchange.model.RegistrationModel;
import com.example.exchange.model.UserModel;

public class UserDao extends BaseDao {
    public ResponseT<UserModel> login(String email, String password) {
        List<String> errors = new ArrayList<>();
        if (email == null || email.isEmpty())
            errors.add("* The email field is required");
        if (password == null || password.isEmpty())
            errors.add("* The password field is required");

        if (!errors.isEmpty())
            return Response.FailT(String.join("\n", errors));

        String query = "SELECT id, email, firstname, lastname FROM \"user\" WHERE LOWER(email)=? AND password=?";
        Map<Integer, Object> parameters = new HashMap<>();
        parameters.put(1, email.trim().toLowerCase());
        parameters.put(2, password);

        ResponseT<UserModel> userResponse = findOne(query, parameters, rs -> {
            try {
                return new UserModel(rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("firstname"),
                        rs.getString("lastname"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });

        if (userResponse.getSucceeded())
            return userResponse.getData() != null ? Response.SuccessT(userResponse.getData())
                    : Response.FailT("The username and password do not match");

        if (!userResponse.getSucceeded())
            return Response.FailT(userResponse.getErrorMessage());

        return Response.SuccessT(null);
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
        insertUserValues.put(4, registration.getPassword());
        
        return write(insertUserQuery, insertUserValues);
    }
}
