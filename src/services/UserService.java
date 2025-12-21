package services;

import util.DBConnection;
import java.sql.*;

public class UserService {

    // Method to handle user login (Existing)
    public models.User login(String username, String pin) {
        String query = "SELECT * FROM users WHERE username = ? AND pin = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, pin);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new models.User(rs.getInt("user_id"), rs.getString("username"), rs.getString("pin"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // NEW: Method to update PIN (Fixes the Dashboard error)
    public boolean updatePin(int userId, String newPin) {
        String query = "UPDATE users SET pin = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, newPin);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}