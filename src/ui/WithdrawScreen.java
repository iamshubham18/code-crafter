package ui;

import models.User;
import services.AccountService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class WithdrawScreen extends JFrame {
    private AccountService accountService = new AccountService();
    private RefreshListener listener;
    private User user; // FIXED: Added class-level variable to store the user

    // Theme Palette
    private final Color BG_COLOR = new Color(25, 25, 35);
    private final Color ACCENT_ROSE = new Color(207, 102, 121);
    private final Color CARD_COLOR = new Color(35, 35, 45);
    private final Color TEXT_PRIMARY = Color.WHITE;

    public WithdrawScreen(User user, RefreshListener listener) {
        this.user = user; // FIXED: Initialize the class variable
        this.listener = listener;

        setUndecorated(true);
        setSize(400, 350);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createLineBorder(ACCENT_ROSE, 1));
        setContentPane(mainPanel);

        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(15, 20, 0, 15));

        JLabel title = new JLabel("WITHDRAW FUNDS");
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setForeground(ACCENT_ROSE);

        JButton closeBtn = new JButton("✕");
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.addActionListener(e -> dispose());

        header.add(title, BorderLayout.WEST);
        header.add(closeBtn, BorderLayout.EAST);
        mainPanel.add(header, BorderLayout.NORTH);

        // --- Center Content ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel prompt = new JLabel("Enter Amount (₹)", SwingConstants.CENTER);
        prompt.setForeground(TEXT_PRIMARY);
        prompt.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        contentPanel.add(prompt, gbc);

        JTextField amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(280, 50));
        amountField.setBackground(CARD_COLOR);
        amountField.setForeground(ACCENT_ROSE);
        amountField.setCaretColor(Color.WHITE);
        amountField.setFont(new Font("Monospaced", Font.BOLD, 24));
        amountField.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridy = 1;
        contentPanel.add(amountField, gbc);

        JButton confirmBtn = new JButton("CONFIRM WITHDRAWAL");
        confirmBtn.setPreferredSize(new Dimension(280, 50));
        confirmBtn.setBackground(ACCENT_ROSE);
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridy = 2;
        gbc.insets = new Insets(25, 0, 0, 0);
        contentPanel.add(confirmBtn, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // --- Withdrawal Logic ---
        confirmBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) return;

                // Now 'user' is accessible here because it is a class field
                if (accountService.withdraw(this.user.getUserId(), amount)) {
                    if (listener != null) {
                        listener.onTransactionComplete();
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient Balance");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });
    }
}