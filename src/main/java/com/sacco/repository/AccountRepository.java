package com.sacco.repository;

import com.sacco.domain.SaccoAccount;
import com.sacco.database.DatabaseManager;
import java.sql.*;
import java.math.*;
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

    public ArrayList<SaccoAccount> findAllByMemberId(long memberId) {
        ArrayList<SaccoAccount> accountsList = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE member_id = ?;";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, memberId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    BigDecimal dbNumeric = rs.getBigDecimal("balance");
                    long balanceInCents = dbNumeric.movePointRight(2).longValue();

                    SaccoAccount account = new SaccoAccount(

                            rs.getLong("account_id"),
                            rs.getLong("member_id"),
                            rs.getString("account_number"),
                            rs.getString("account_type"),
                            balanceInCents,
                            rs.getString("account_status"),
                            rs.getTimestamp("created_at").toLocalDateTime());
                    accountsList.add(account);

                }

            }

        } catch (SQLException e) {

            System.out.println("Error: Check AccountRepository.findAllByMemberId: " + e.getMessage());
        }

        return accountsList;

    }

    public void updateBalance(Connection conn, long accountId, long newBalanceInCents) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            java.math.BigDecimal databaseDecimal = java.math.BigDecimal.valueOf(newBalanceInCents, 2);
            stmt.setBigDecimal(1, databaseDecimal);
            stmt.setLong(2, accountId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException(
                        "Account modification failed. Target Account ID " + accountId + " not found on disk.");
            }
        }
    }

}
