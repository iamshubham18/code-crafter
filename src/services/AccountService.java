package services;

import util.DBConnection;
import java.sql.*;

public class AccountService {

    // 1. Fetch Current Balance
    public double getBalance(int userId) {
        String query = "SELECT balance FROM accounts WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble("balance");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // 2. Optimized Deposit with Transaction Integrity
    public boolean deposit(int userId, double amount) {
        String updateSQL = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?";
        String logSQL = "INSERT INTO transactions (account_number, type, amount) VALUES (?, 'DEPOSIT', ?)";

        return executeTransaction(userId, amount, updateSQL, logSQL);
    }

    // 3. Optimized Withdrawal with Transaction Integrity
    public boolean withdraw(int userId, double amount) {
        if (getBalance(userId) < amount) return false;

        String updateSQL = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?";
        String logSQL = "INSERT INTO transactions (account_number, type, amount) VALUES (?, 'WITHDRAWAL', ?)";

        return executeTransaction(userId, amount, updateSQL, logSQL);
    }

    /**
     * CORE LOGIC: Handles the atomic operation of updating balance AND logging history.
     */
    private boolean executeTransaction(int userId, double amount, String updateSQL, String logSQL) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // START TRANSACTION

            // 1. Update Balance
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                updateStmt.setDouble(1, amount);
                updateStmt.setInt(2, userId);
                updateStmt.executeUpdate();
            }

            // 2. Get Account Number
            String accNum = "";
            String accQuery = "SELECT account_number FROM accounts WHERE user_id = ?";
            try (PreparedStatement accStmt = conn.prepareStatement(accQuery)) {
                accStmt.setInt(1, userId);
                ResultSet rs = accStmt.executeQuery();
                if (rs.next()) accNum = rs.getString("account_number");
            }

            // 3. Log Transaction
            try (PreparedStatement logStmt = conn.prepareStatement(logSQL)) {
                logStmt.setString(1, accNum);
                logStmt.setDouble(2, amount);
                logStmt.executeUpdate();
            }

            conn.commit(); // SAVE BOTH
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}