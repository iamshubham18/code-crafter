package services;

import util.DBConnection;
import java.sql.*;

public class TransactionService {

    /**
     * Records a new transaction in the database.
     * @param accNum The account number involved.
     * @param type The type (DEPOSIT or WITHDRAWAL).
     * @param amount The amount transferred.
     */
    public void recordTransaction(String accNum, String type, double amount) {
        String query = "INSERT INTO transactions (account_number, type, amount) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, accNum);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error recording transaction: " + e.getMessage());
        }
    }

    /**
     * Fetches all transactions for a specific user by joining accounts and transactions.
     * This is used by the TransactionHistoryScreen JTable.
     */
    public ResultSet getTransactionsByUserId(int userId) {
        // We use a JOIN because the transactions table stores account_number, not user_id
        String query = "SELECT t.type, t.amount, t.timestamp FROM transactions t " +
                "JOIN accounts a ON t.account_number = a.account_number " +
                "WHERE a.user_id = ? ORDER BY t.timestamp DESC";
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}