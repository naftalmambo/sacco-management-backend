package com.sacco.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:postgresql://localhost:5432/sacco_management_db";
    private static final String USER = "sacco_admin";
    private static final String PASSWORD = "mamboSecure2012";

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("CRITICAL: Postgresql Database Driver Class missing from classpath!", e);
        }

    }

}
