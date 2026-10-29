package com.sacco.service;

import com.sacco.domain.*;
import com.sacco.repository.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SaccoEngineService {

    private final MemberRepository memberRepo = new MemberRepository();
    private final AccountRepository accountRepo = new AccountRepository();
    private final TransactionRepository transactionRepo = new TransactionRepository();
    private final LoanRepository loanRepo = new LoanRepository();

    public void runMemberAuditReport(long memberId) {
        SaccoMember member = memberRepo.findById(memberId);

        if (member == null) {
            System.out.println("CRITICAL ERROR: Member record with ID " + memberId + " missing from disk.");
            return;
        }

        System.out.println("\n[MEMBER IDENTITY DUMP]");
        System.out.println(member);

        System.out.println("\n[FINANCIAL ACCOUNT MANAGEMENT SYSTEM]");
        ArrayList<SaccoAccount> activeAccounts = accountRepo.findAllByMemberId(memberId);

        if (activeAccounts.isEmpty()) {
            System.out.println("No active financial accounts registered for this member.");
        } else {
            for (SaccoAccount account : activeAccounts) {
                System.out.println(account);

                ArrayList<LedgerTransaction> recentLedgerEntries = transactionRepo
                        .findRecentByAccountId(account.getAccountId());
                for (LedgerTransaction entry : recentLedgerEntries) {
                    System.out.println("   └─ " + entry);
                }
            }
        }

        System.out.println("\n[CREDIT REGISTRY ANALYSIS]");
        ArrayList<Loan> memberLoans = loanRepo.findAllByMemberId(memberId);

        if (memberLoans.isEmpty()) {
            System.out.println("No credit facility logs found for this member.");
        } else {
            for (Loan creditRecord : memberLoans) {
                System.out.println(creditRecord);
            }
        }
    }

    public void processCashWithdrawal(long accountId, long amountInCents) {
        if (amountInCents <= 0) {
            System.out.println("TRANSACTION BLOCKED: Withdrawal amount must be greater than zero.");
            return;
        }

        try (Connection conn = com.sacco.database.DatabaseManager.getConnection()) {

            conn.setAutoCommit(false);

            try {

                SaccoAccount account = null;
                String lookupSql = "SELECT * FROM accounts WHERE account_id = ?;";
                try (PreparedStatement stmt = conn.prepareStatement(lookupSql)) {
                    stmt.setLong(1, accountId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            java.math.BigDecimal dbNumeric = rs.getBigDecimal("balance");
                            long balanceInCents = dbNumeric.movePointRight(2).longValue();
                            account = new SaccoAccount(
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

                if (account == null) {
                    throw new SQLException(
                            "Security Alert: Target Account ID " + accountId + " does not exist on disk.");
                }

                if ("SHARES".equalsIgnoreCase(account.getAccountType())
                        || "LOAN".equalsIgnoreCase(account.getAccountType())) {
                    throw new SQLException(
                            "Transaction Violation: Direct cash withdrawals are strictly restricted to SAVINGS accounts.");
                }

                long currentBalance = account.getBalanceInCents();
                long newBalance = currentBalance - amountInCents;

                long minimumFloorLimit = 100000L;

                if (newBalance < minimumFloorLimit) {
                    throw new SQLException(
                            "Insufficient Liquidity: Request denied. Savings account must maintain a baseline floor of 1,000.00 KSh.");
                }

                accountRepo.updateBalance(conn, accountId, newBalance);

                transactionRepo.insertTransaction(conn, accountId, null, "WITHDRAWAL", amountInCents, "KES");

                conn.commit();
                System.out.println("TRANSACTION SUCCESSFUL: " + (amountInCents / 100.0)
                        + " KSh withdrawn from Account ID " + accountId);

            } catch (SQLException e) {

                conn.rollback();
                System.out.println("🚨 EMERGENCY ROLLBACK TRIGGERED: " + e.getMessage());
            } finally {

                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Critical System Database Drop Exception: " + e.getMessage());
        }
    }

}
