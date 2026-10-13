package com.sacco;

import com.sacco.domain.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.*;

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/sacco_management_db";
        String user = "sacco_admin";
        String password = "mamboSecure2012";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("SUCCESS: Direct SACCO Database Connection Established!");

            // 1. Fetch Member Profile
            String query = "SELECT * FROM members WHERE member_id = 1;";

            try (Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query)) {

                if (rs.next()) {
                    long memberId = rs.getLong("member_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();
                    String sex = rs.getString("sex");
                    String emailAddress = rs.getString("email_address");
                    String phoneNumber = rs.getString("phone_number");
                    String nationalId = rs.getString("national_id");
                    String county = rs.getString("county");
                    LocalDate joinedDate = rs.getDate("joined_date").toLocalDate();

                    SaccoMember memberOne = new SaccoMember(memberId, firstName, lastName, dateOfBirth, sex,
                            emailAddress, phoneNumber, nationalId, county, joinedDate);
                    System.out.println(memberOne);

                    // 2. Fetch all Accounts and their nested Transactions
                    String accountQuery = "SELECT * FROM accounts WHERE member_id = 1;";

                    try (Statement accStmt = conn.createStatement();
                            ResultSet accRs = accStmt.executeQuery(accountQuery)) {

                        while (accRs.next()) {
                            long accountId = accRs.getLong("account_id");
                            String accountNumber = accRs.getString("account_number");
                            long accmemberId = accRs.getLong("member_id");
                            String accountType = accRs.getString("account_type");
                            long balanceInCents = (long) (accRs.getDouble("balance") * 100);

                            SaccoAccount accountOne = new SaccoAccount(accountNumber, accmemberId, accountType,
                                    balanceInCents);
                            System.out.println(accountOne);

                            String transactionQuery = "SELECT * FROM transactions WHERE account_id = "
                                    + accountId + ";";

                            try (Statement transactionStmt = conn.createStatement();
                                    ResultSet transRs = transactionStmt.executeQuery(transactionQuery)) {

                                while (transRs.next()) {
                                    long transactionId = transRs.getLong("transaction_id");
                                    String transactionType = transRs.getString("transaction_type");
                                    long amountInCents = (long) (transRs.getDouble("amount") * 100);
                                    LocalDateTime createdAt = transRs.getTimestamp("created_at").toLocalDateTime();

                                    LedgerTransaction transMemberOne = new LedgerTransaction(transactionId, accountId,
                                            transactionType, amountInCents, createdAt);

                                    System.out.println("   └─ " + transMemberOne);
                                }
                            }
                        }
                    }

                    System.out.println("\n--- Fetching Member Credit Records ---");
                    String loanQuery = "SELECT * FROM loans WHERE member_id = " + memberId + ";";

                    try (Statement loanStmt = conn.createStatement();
                            ResultSet loanRs = loanStmt.executeQuery(loanQuery)) {

                        while (loanRs.next()) {
                            long loanId = loanRs.getLong("loan_id");
                            long loanMemberId = loanRs.getLong("member_id");

                            double principalDecimal = loanRs.getDouble("principal_amount");
                            long principleAmountInCents = (long) (principalDecimal * 100);

                            double interestRate = loanRs.getDouble("interest_rate");
                            long interestOwedInCents = loanRs.getLong("interest_owed");
                            String loanStatus = loanRs.getString("loan_status");

                            LocalDateTime appliedAt = loanRs.getTimestamp("applied_at").toLocalDateTime();

                            LocalDateTime disbursedAt = null;
                            Timestamp disbursedTimestamp = loanRs.getTimestamp("disbursed_at");
                            if (disbursedTimestamp != null) {
                                disbursedAt = disbursedTimestamp.toLocalDateTime();
                            }

                            Loan loanOne = new Loan(
                                    loanId,
                                    loanMemberId,
                                    principleAmountInCents,
                                    interestRate,
                                    interestOwedInCents,
                                    loanStatus,
                                    appliedAt,
                                    disbursedAt);

                            System.out.println(loanOne);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("CRITICAL FAILURE: Database Connection Denied!");
            e.printStackTrace();
        }
    }
}
