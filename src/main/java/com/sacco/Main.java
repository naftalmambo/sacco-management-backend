package com.sacco;

import com.sacco.domain.*;
import java.time.LocalDate;
import java.sql.*;

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/sacco_management_db";
        String user = "sacco_admin";
        String password = "mamboSecure2012";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("SUCCESS: Direct SACCO Database Connection Established!");

        } catch (SQLException e) {
            System.out.println("CRITICAL FAILURE: Database Connection Denied!");
            e.printStackTrace();

        }

    }

}
