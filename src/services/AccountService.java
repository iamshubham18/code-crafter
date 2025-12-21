package services;

import util.DBConnection;
import java.sql.*;

public class AccountService {
    // We delegate the history recording to the TransactionService
    private TransactionService txService = new TransactionService();

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

    // 2. Deposit Money & Record Transaction
    public boolean deposit(int userId, double amount) {
        String query = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);

            if (pstmt.executeUpdate() > 0) {
                // After balance update, record the transaction history
                String accNum = getAccountNumber(userId);
                txService.recordTransaction(accNum, "DEPOSIT", amount);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Withdraw Money & Record Transaction
    public boolean withdraw(int userId, double amount) {
        if (getBalance(userId) < amount) return false; // Validation

        String query = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);

            if (pstmt.executeUpdate() > 0) {
                // Record the withdrawal in history
                String accNum = getAccountNumber(userId);
                txService.recordTransaction(accNum, "WITHDRAWAL", amount);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // HELPER: Get Account Number from User ID
    // This is needed because the 'transactions' table uses account_number as a Foreign Key
    public String getAccountNumber(int userId) {
        String query = "SELECT account_number FROM accounts WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getString("account_number");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}