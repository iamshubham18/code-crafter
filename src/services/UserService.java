package services;

import util.DBConnection;
import models.User;
import java.sql.*;

public class UserService {

    // Method to handle user login
    public User login(String username, String pin) {
        String query = "SELECT * FROM users WHERE username = ? AND pin = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, pin);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("pin"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * NEW: Registers a new user and creates their initial bank account.
     * Uses a Transaction (commit/rollback) to ensure data integrity.
     */
    public boolean register(String username, String pin, String accountNumber, double initialBalance) {
        String userSQL = "INSERT INTO users (username, pin) VALUES (?, ?)";
        String accountSQL = "INSERT INTO accounts (account_number, user_id, balance) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            int userId = -1;
            // 1. Insert User and get the auto-generated user_id
            try (PreparedStatement pstmtUser = conn.prepareStatement(userSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmtUser.setString(1, username);
                pstmtUser.setString(2, pin);
                pstmtUser.executeUpdate();

                ResultSet rs = pstmtUser.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);
                }
            }

            // 2. Link the Account to the newly created User
            if (userId != -1) {
                try (PreparedStatement pstmtAcc = conn.prepareStatement(accountSQL)) {
                    pstmtAcc.setString(1, accountNumber);
                    pstmtAcc.setInt(2, userId);
                    pstmtAcc.setDouble(3, initialBalance);
                    pstmtAcc.executeUpdate();
                }
            }

            conn.commit(); // Save changes to both tables
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Registration Error: " + e.getMessage());
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // Method to update PIN
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