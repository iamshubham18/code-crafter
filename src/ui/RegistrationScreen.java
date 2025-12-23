package ui;

import services.UserService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class RegistrationScreen extends JFrame {
    private UserService userService = new UserService();
    private final Color BG_DARK = new Color(18, 18, 18);
    private final Color ACCENT_PURPLE = new Color(187, 134, 252);
    private final Color FIELD_BG = new Color(30, 30, 38);
    private Point initialClick;

    public RegistrationScreen() {
        setUndecorated(true);
        setSize(450, 650); // Tall enough for all registration fields
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(BorderFactory.createLineBorder(ACCENT_PURPLE, 1));
        mainPanel.setBorder(new EmptyBorder(20, 45, 30, 45));

        // --- Header ---
        JLabel header = new JLabel("Create Account");
        header.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.setForeground(Color.WHITE);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Fields ---
        JTextField userField = new JTextField();
        styleField(userField);

        JPasswordField pinField = new JPasswordField();
        styleField(pinField);

        JTextField accField = new JTextField();
        styleField(accField);

        JTextField balField = new JTextField();
        styleField(balField);

        // --- Register Button ---
        JButton regBtn = new JButton("REGISTER");
        styleButton(regBtn);

        // --- Back to Login ---
        JButton backBtn = new JButton("Back to Login");
        backBtn.setForeground(ACCENT_PURPLE);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- Assembly ---
        mainPanel.add(header);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(createLabel("Username"));
        mainPanel.add(userField);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createLabel("Security PIN"));
        mainPanel.add(pinField);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createLabel("New Account Number"));
        mainPanel.add(accField);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createLabel("Initial Deposit (₹)"));
        mainPanel.add(balField);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(regBtn);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(backBtn);

        add(mainPanel);

        // --- Actions ---
        regBtn.addActionListener(e -> {
            String user = userField.getText();
            String pin = new String(pinField.getPassword());
            String acc = accField.getText();
            double bal;

            try {
                bal = Double.parseDouble(balField.getText());
                boolean success = userService.register(user, pin, acc, bal);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Registration Successful!");
                    new LoginScreen().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Registration Failed. Check if account number exists.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
            }
        });

        backBtn.addActionListener(e -> {
            new LoginScreen().setVisible(true);
            dispose();
        });

        enableDragging(mainPanel);
    }

    // Reuse your styling methods to keep the UI consistent
    private void styleField(JTextField field) {
        field.setMaximumSize(new Dimension(350, 40));
        field.setBackground(FIELD_BG);
        field.setForeground(Color.WHITE);
        field.setCaretColor(ACCENT_PURPLE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80), 1),
                new EmptyBorder(0, 10, 0, 10)));
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(170, 170, 170));
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private void styleButton(JButton btn) {
        btn.setBackground(ACCENT_PURPLE);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setMaximumSize(new Dimension(350, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
    }

    private void enableDragging(JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { initialClick = e.getPoint(); }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(getLocation().x + e.getX() - initialClick.x, getLocation().y + e.getY() - initialClick.y);
            }
        });
    }
}