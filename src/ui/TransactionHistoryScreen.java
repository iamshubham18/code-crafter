package ui;

import models.User;
import services.TransactionService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class TransactionHistoryScreen extends JFrame {
    private TransactionService txService = new TransactionService();

    public TransactionHistoryScreen(User user) {
        setTitle("Transaction History - " + user.getUsername());
        setSize(550, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table column headers
        String[] columnNames = {"Type", "Amount ($)", "Date & Time"};

        // Model to hold table data
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        // Fetching data from TransactionService
        try {
            ResultSet rs = txService.getTransactionsByUserId(user.getUserId());
            while (rs != null && rs.next()) {
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                String timestamp = rs.getTimestamp("timestamp").toString();

                // Adding row to table model
                model.addRow(new Object[]{type, amount, timestamp});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading history: " + e.getMessage());
        }

        // Add table to scroll pane
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Close button
        JButton closeBtn = new JButton("Back to Dashboard");
        closeBtn.addActionListener(e -> dispose());
        add(closeBtn, BorderLayout.SOUTH);
    }
}