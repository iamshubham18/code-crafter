package ui;

import models.User;
import services.AccountService;
import javax.swing.*;
import java.awt.*;

public class DepositScreen extends JFrame {
    private AccountService accountService = new AccountService();

    public DepositScreen(User user) {
        setTitle("Deposit Cash");
        setSize(300, 200);
        setLayout(new GridLayout(3, 1, 10, 10));
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Enter Amount to Deposit:", SwingConstants.CENTER);
        JTextField amountField = new JTextField();
        JButton confirmBtn = new JButton("Confirm Deposit");

        add(label);
        add(amountField);
        add(confirmBtn);

        confirmBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Please enter an amount greater than 0.");
                    return;
                }

                boolean success = accountService.deposit(user.getUserId(), amount);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Deposit Successful! Amount: $" + amount);
                    dispose(); // Returns user to the Dashboard
                } else {
                    JOptionPane.showMessageDialog(this, "Database Error. Try again.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number.");
            }
        });
    }
}