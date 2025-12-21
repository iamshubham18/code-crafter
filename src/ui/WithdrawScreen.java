package ui;

import models.User;
import services.AccountService;
import javax.swing.*;
import java.awt.*;

public class WithdrawScreen extends JFrame {
    private AccountService accountService = new AccountService();

    public WithdrawScreen(User user) {
        // Window Setup
        setTitle("Withdraw Cash - " + user.getUsername());
        setSize(350, 250);
        setLayout(new GridLayout(4, 1, 10, 10));
        setLocationRelativeTo(null);

        // UI Components
        JLabel label = new JLabel("Enter Withdrawal Amount:", SwingConstants.CENTER);
        JTextField amountField = new JTextField();
        JButton confirmBtn = new JButton("Confirm Withdrawal");
        JButton backBtn = new JButton("Back");

        // Styling
        confirmBtn.setBackground(new Color(255, 102, 102)); // Reddish for "Withdraw"

        // Logic
        confirmBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());

                // Logic check for positive numbers
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Enter a valid amount.");
                    return;
                }

                // Call AccountService to update DB
                boolean success = accountService.withdraw(user.getUserId(), amount);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Withdrawal Successful!");
                    dispose(); // Close this window
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient Funds or DB Error!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.");
            }
        });

        backBtn.addActionListener(e -> dispose());

        // Add to frame
        add(label);
        add(amountField);
        add(confirmBtn);
        add(backBtn);
    }
}