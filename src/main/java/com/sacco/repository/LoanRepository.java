package com.sacco.repository;

import com.sacco.domain.Loan;
import com.sacco.database.DatabaseManager;
import java.sql.*;
import java.math.*;
import java.util.ArrayList;

public class LoanRepository {

    public void save(Loan creditLine) {
        String sql = "INSERT INTO loans (member_id, principal_amount, interest_rate, interest_owed) VALUES (?, ?, ?, ?);";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, creditLine.getMemberId());

            java.math.BigDecimal principalDecimal = java.math.BigDecimal.valueOf(creditLine.getPrincipleAmountInCents(),
                    2);
            stmt.setBigDecimal(2, principalDecimal);

            java.math.BigDecimal rateDecimal = java.math.BigDecimal.valueOf(creditLine.getInterestRate());
            stmt.setBigDecimal(3, rateDecimal);

            stmt.setLong(4, creditLine.getInterestOwedInCents());

            stmt.executeUpdate();
            System.out.println("SUCCESS: Credit registry application permanently written to disk!");

        } catch (SQLException e) {
            System.out.println("Error: Check LoanRepository.save: " + e.getMessage());
        }
    }

}
