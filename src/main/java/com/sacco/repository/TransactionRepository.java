package com.sacco.repository;

import com.sacco.domain.LedgerTransaction;
import com.sacco.database.DatabaseManager;
import java.sql.*;
import java.math.*;
import java.util.ArrayList;

public class TransactionRepository {

    public void save(LedgerTransaction transaction) {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount) VALUES (?, ?, ?);";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, transaction.getAccountId());
            stmt.setString(2, transaction.getTransactionType().toUpperCase());

            java.math.BigDecimal databaseDecimal = java.math.BigDecimal.valueOf(transaction.getAmountInCents(), 2);
            stmt.setBigDecimal(3, databaseDecimal);

            stmt.executeUpdate();
            System.out.println("SUCCESS: Transaction history ledger record written to disk!");

        } catch (SQLException e) {
            System.out.println("Error: Check TransactionRepository.save: " + e.getMessage());
        }
    }

    public ArrayList<LedgerTransaction> findRecentByAccountId(long accountId) {
        ArrayList<LedgerTransaction> transactionList = new ArrayList<>();

        String sql = "SELECT * FROM transactions WHERE account_Id = ? ORDER BY created_at DESC LIMIT 10;";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    BigDecimal dbNumeric = rs.getBigDecimal("amount");
                    long amountInCents = dbNumeric.movePointRight(2).longValue();

                    LedgerTransaction transaction = new LedgerTransaction(
                            rs.getLong("transaction_id"),
                            rs.getLong("account_id"),
                            rs.getString("transaction_type"),
                            amountInCents,
                            rs.getTimestamp("created_at").toLocalDateTime()

                    );
                    transactionList.add(transaction);

                }

            }

        } catch (SQLException e) {
            System.out.println("Error: Check TransactionRepository.findRecentByAccountId: " + e.getMessage());

        }
        return transactionList;

    }

}