package com.sacco.repository;

import com.sacco.domain.SaccoAccount;
import com.sacco.database.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;

public class AccountRepository {

    public void save(SaccoAccount NewAccount) {

        String sql = "INSERT INTO accounts (member_id, account_number, account_type, balance) VALUES (?, ?, ?, ?);";

        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, NewAccount.getMemberId());
            stmt.setString(2, NewAccount.getAccountNumber());
            stmt.setString(3, NewAccount.getAccountType());
            java.math.BigDecimal databaseDecimal = java.math.BigDecimal.valueOf(NewAccount.getBalanceInCents(), 2);
            stmt.setBigDecimal(4, databaseDecimal);
            stmt.executeUpdate();
            System.out.println("SUCCESS: New account written to disk!");

        } catch (SQLException e) {
            System.out.println("Error: Check AccountRepository.save: " + e.getMessage());

        }

    }
}
