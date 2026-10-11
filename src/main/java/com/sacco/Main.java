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

                    SaccoMember memberOne = new SaccoMember(memberId, firstName, lastName, dateOfBirth, sex,
                            emailAddress,
                            phoneNumber, nationalId, county);
                    System.out.println(memberOne);

                    String accountQuery = "SELECT * FROM accounts WHERE member_id = 1;";

                    try (Statement accStmt = conn.createStatement();
                            ResultSet accRs = accStmt.executeQuery(accountQuery)) {

                        while (accRs.next()) {
                            String accountNumber = accRs.getString("account_number");
                            long accmemberId = accRs.getLong("member_id");
                            String accountType = accRs.getString("account_type");
                            long balanceInCents = (long) (accRs.getDouble("balance") * 100);

                            SaccoAccount accountOne = new SaccoAccount(accountNumber, accmemberId, accountType,
                                    balanceInCents);
                            System.out.println(accountOne);

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
