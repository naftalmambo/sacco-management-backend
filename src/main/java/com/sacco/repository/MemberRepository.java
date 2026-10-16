package com.sacco.repository;

import com.sacco.domain.SaccoMember;
import com.sacco.database.DatabaseManager;
import java.sql.*;

public class MemberRepository {

    public void save(SaccoMember member) {
        String sql = "INSERT INTO members (first_name, last_name, date_of_birth, sex, email_address, phone_number, national_id, county) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, member.getFirstName());
            stmt.setString(2, member.getLastName());
            stmt.setDate(3, java.sql.Date.valueOf(member.getDateOfBirth()));
            stmt.setString(4, member.getSex());
            stmt.setString(5, member.getEmailAddress());
            stmt.setString(6, member.getPhoneNumber());
            stmt.setString(7, member.getNationalId());
            stmt.setString(8, member.getCounty());

            stmt.executeUpdate();
            System.out.println("SUCCESS: New member written to disk!");
        } catch (SQLException e) {
            System.out.println("ERROR: Check MemberRepository.save: " + e.getMessage());
        }

    }

    public SaccoMember findByNationalId(String nationalId) {
        String sql = "SELECT * FROM members WHERE national_id = ?;";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nationalId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SaccoMember(
                            rs.getLong("member_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getDate("date_of_birth").toLocalDate(),
                            rs.getString("sex"),
                            rs.getString("email_address"),
                            rs.getString("phone_number"),
                            rs.getString("national_id"),
                            rs.getString("county"),
                            rs.getTimestamp("joined_date").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Inside MemberRepository.findByNationalId: " + e.getMessage());
        }
        return null;
    }

    public SaccoMember findById(long memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?;";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, memberId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SaccoMember(
                            rs.getLong("member_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getDate("date_of_birth").toLocalDate(),
                            rs.getString("sex"),
                            rs.getString("email_address"),
                            rs.getString("phone_number"),
                            rs.getString("national_id"),
                            rs.getString("county"),
                            rs.getTimestamp("joined_date").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR: Inside MemberRepository.findById: " + e.getMessage());
        }
        return null;
    }

}