package com.example.exchange.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.example.exchange.helper.Response;
import com.example.exchange.helper.ResponseT;

public abstract class BaseDao {
    private static final String URL = "jdbc:postgresql://localhost:5432/exchange";
    private static final String USER = "worinium";
    private static final String PASSWORD = "worisoft@123";
    private static final String DRIVER = "org.postgresql.Driver";

    protected Connection getConnection() throws SQLException, ClassNotFoundException {
        Connection connection = null;
        Class.forName(DRIVER);
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        return connection;
    }

    protected Response write(String query, Map<Integer, Object> parameters) {
        try {
            try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
                if (parameters != null) {
                    for (Map.Entry<Integer, Object> parameter : parameters.entrySet()) {
                        stmt.setObject(parameter.getKey(), parameter.getValue());
                    }
                }

                stmt.executeUpdate();

                return Response.Success();
            }
        } catch (Exception ex) {
            return Response.Fail(ex.getMessage());
        }
    }

    protected <T> ResponseT<T> findOne(String query, Map<Integer, Object> parameters,
            Function<ResultSet, T> func) {
        try {
            try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
                if (parameters != null) {
                    for (Map.Entry<Integer, Object> parameter : parameters.entrySet()) {
                        stmt.setObject(parameter.getKey(), parameter.getValue());
                    }
                }

                ResultSet rs = stmt.executeQuery();
                if (rs.isBeforeFirst()) {
                    rs.next();

                    T entity = func.apply(rs);
                    return Response.SuccessT(entity);
                }

                return Response.SuccessT(null);
            }
        } catch (Exception ex) {
            return Response.FailT(ex.getMessage());
        }
    }

    protected <T> ResponseT<List<T>> findMany(String query, Map<Integer, Object> parameters,
            Function<ResultSet, T> func) {
        try {
            try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
                if (parameters != null) {
                    for (Map.Entry<Integer, Object> parameter : parameters.entrySet()) {
                        stmt.setObject(parameter.getKey(), parameter.getValue());
                    }
                }

                List<T> result = new ArrayList<>();

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    T entity = func.apply(rs);
                    result.add(entity);
                }

                return Response.SuccessT(result);
            }
        } catch (Exception ex) {
            return Response.FailT(ex.getMessage());
        }
    }
}