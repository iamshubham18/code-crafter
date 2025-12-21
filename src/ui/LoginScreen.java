package ui;

import services.UserService;
import models.User;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginScreen extends JFrame {
    private UserService userService = new UserService();

    public LoginScreen() {
        // 1. Window Configuration
        setTitle("Global Digital Bank - Secure ATM");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Container using a 1x2 Grid for the Split-Pane look
        JPanel container = new JPanel(new GridLayout(1, 2));

        // --- LEFT SIDE: Branding Panel ---
        JPanel brandPanel = new JPanel(new GridBagLayout());
        brandPanel.setBackground(new Color(41, 128, 185)); // Deep Professional Blue

        // Using HTML for multi-line centered text
        JLabel brandLogo = new JLabel("<html><center><font size='7'>Phoneix</font><br>ATM System</center></html>");
        brandLogo.setFont(new Font("Serif", Font.PLAIN, 24));
        brandLogo.setForeground(Color.WHITE);
        brandPanel.add(brandLogo);

        // --- RIGHT SIDE: Login Form ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(40, 30, 40, 30));
        formPanel.setBackground(Color.WHITE);

        // Header Labels
        JLabel loginHeader = new JLabel("Welcome Back");
        loginHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        loginHeader.setAlignmentX(Component.LEFT_ALIGNMENT); // Fixed: All Caps

        JLabel subHeader = new JLabel("Please enter your details to continue");
        subHeader.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subHeader.setForeground(Color.GRAY);
        subHeader.setAlignmentX(Component.LEFT_ALIGNMENT); // Fixed: All Caps

        // Input Fields
        JTextField userField = new JTextField();
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JPasswordField pinField = new JPasswordField();
        pinField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));


        // Action Button Styling
        JButton loginBtn = new JButton("Sign In");
        loginBtn.setBackground(new Color(41, 128, 185)); // Sets button background to Blue
        loginBtn.setForeground(Color.WHITE);             // Sets text color to White
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setOpaque(true);                         // Ensures the color is solid
        loginBtn.setBorderPainted(false);                 // Clean, flat look
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginBtn.setFocusPainted(false);

        // --- Assembly ---
        formPanel.add(loginHeader);
        formPanel.add(subHeader);
        formPanel.add(Box.createVerticalStrut(30)); // Spacer

        JLabel uLabel = new JLabel("Username");
        uLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(uLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(userField);

        formPanel.add(Box.createVerticalStrut(15));

        JLabel pLabel = new JLabel("PIN");
        pLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(pLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(pinField);

        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(loginBtn);

        // Login Logic
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String pin = new String(pinField.getPassword());
            User authenticatedUser = userService.login(username, pin);

            if (authenticatedUser != null) {
                new Dashboard(authenticatedUser).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or PIN", "Authentication Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Combine Panels
        container.add(brandPanel);
        container.add(formPanel);

        add(container);
    }
}