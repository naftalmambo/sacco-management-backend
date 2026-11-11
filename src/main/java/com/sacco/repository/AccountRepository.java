package com.sacco.repository;

import com.sacco.domain.SaccoAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<SaccoAccount, Long> {
    List<SaccoAccount> findAllByMemberId(Long memberId);

    Optional<SaccoAccount> findByMemberIdAndAccountType(Long memberId, String accountType);

    Optional<SaccoAccount> findByAccountNumber(String accountNumber);

    default void updateBalance(Connection conn, long accountId, long newBalanceInCents) throws SQLException {
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

    default SaccoAccount findSavingsAccountByMemberId(Connection conn, long memberId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE member_id = ? AND account_type = 'SAVINGS' AND account_status = 'ACTIVE';";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, memberId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    java.math.BigDecimal dbNumeric = rs.getBigDecimal("balance");
                    long balanceInCents = dbNumeric.movePointRight(2).longValue();

                    return new SaccoAccount(
                            rs.getLong("account_id"),
                            rs.getLong("member_id"),
                            rs.getString("account_number"),
                            rs.getString("account_type"),
                            balanceInCents,
                            rs.getString("account_status"),
                            rs.getTimestamp("created_at").toLocalDateTime());
                }
            }
        }
        return null;
    }

    default SaccoAccount findLoanAccountByMemberId(Connection conn, long memberId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE member_id = ? AND account_type = 'LOAN' AND account_status = 'ACTIVE';";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, memberId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    java.math.BigDecimal dbNumeric = rs.getBigDecimal("balance");
                    long balanceInCents = dbNumeric.movePointRight(2).longValue();

                    return new SaccoAccount(
                            rs.getLong("account_id"),
                            rs.getLong("member_id"),
                            rs.getString("account_number"),
                            rs.getString("account_type"),
                            balanceInCents,
                            rs.getString("account_status"),
                            rs.getTimestamp("created_at").toLocalDateTime());
                }
            }
        }
        return null;
    }

}