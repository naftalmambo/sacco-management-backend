import com.sacco.domain.SaccoMember;
import com.sacco.database.DatabaseManager;
import java.sql.*;
import java.time.LocalDate;

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
            System.out.println("ERROR: Inside MemberRepository.save: " + e.getMessage());
        }

    }

}