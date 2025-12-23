package ui;

import models.User;
import services.UserService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginScreen extends JFrame {
    private UserService userService = new UserService();
    private final Color BG_DARK = new Color(18, 18, 18);
    private final Color BRAND_PANEL_BG = new Color(25, 25, 32);
    private final Color ACCENT_PURPLE = new Color(187, 134, 252);
    private final Color FIELD_BG = new Color(30, 30, 38);
    private Point initialClick;
    private float frameOpacity = 0.0f;

    public LoginScreen() {
        setUndecorated(true);
        setOpacity(0.0f); // Start invisible for animation
        setSize(750, 500);
        setLocationRelativeTo(null);

        JPanel container = new JPanel(new GridLayout(1, 2));
        container.setBorder(BorderFactory.createLineBorder(ACCENT_PURPLE, 1));

        // --- LEFT SIDE: Brand ---
        JPanel brandPanel = new JPanel(new GridBagLayout());
        brandPanel.setBackground(BRAND_PANEL_BG);
        brandPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(50, 50, 50)));
        JLabel brandLogo = new JLabel("<html><center><font size='6' color='#BB86FC'>Code Crafters</font><br><font color='#A0A0A0'>ATM SYSTEM</font></center></html>");
        brandPanel.add(brandLogo);

        // --- RIGHT SIDE: Form ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(10, 45, 30, 45));
        formPanel.setBackground(BG_DARK);

        // Exit Header
        JPanel exitRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitRow.setOpaque(false);
        JButton exitBtn = new JButton("✕");
        exitBtn.setForeground(Color.GRAY);
        exitBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        exitBtn.setContentAreaFilled(false);
        exitBtn.setBorderPainted(false);
        exitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitBtn.addActionListener(e -> System.exit(0));
        exitRow.add(exitBtn);

        // Header Labels
        JLabel loginHeader = new JLabel("Welcome Back");
        loginHeader.setFont(new Font("SansSerif", Font.BOLD, 28));
        loginHeader.setForeground(Color.WHITE);
        loginHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input Fields
        JTextField userField = new JTextField();
        styleLargeField(userField);

        JPasswordField pinField = new JPasswordField();
        styleLargeField(pinField);

        // Login Button
        JButton loginBtn = new JButton("SIGN IN");
        styleButton(loginBtn, ACCENT_PURPLE);

        // Registration Link
        JButton registerBtn = new JButton("Don't have an account? Register here");
        registerBtn.setForeground(ACCENT_PURPLE);
        registerBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        registerBtn.setContentAreaFilled(false);
        registerBtn.setBorderPainted(false);
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Assembly ---
        formPanel.add(exitRow);
        formPanel.add(Box.createVerticalGlue());
        formPanel.add(loginHeader);
        formPanel.add(Box.createVerticalStrut(40));
        formPanel.add(createFieldLabel("Username"));
        formPanel.add(userField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(createFieldLabel("Security PIN"));
        formPanel.add(pinField);
        formPanel.add(Box.createVerticalStrut(40));
        formPanel.add(loginBtn);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(registerBtn);
        formPanel.add(Box.createVerticalGlue());

        // --- Listeners ---
        loginBtn.addActionListener(e -> {
            User authUser = userService.login(userField.getText(), new String(pinField.getPassword()));
            if (authUser != null) {
                // If Dashboard.java is in the same 'ui' package, this will now compile
                new Dashboard(authUser).setVisible(true);
                dispose();
            } else {
                showThemedError("Invalid Credentials");
            }
        });

        registerBtn.addActionListener(e -> {
            new RegistrationScreen().setVisible(true);
            dispose();
        });

        container.add(brandPanel);
        container.add(formPanel);
        add(container);

        enableDragging(container);
        startEntranceAnimation();
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setMaximumSize(new Dimension(320, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void startEntranceAnimation() {
        Timer timer = new Timer(20, e -> {
            frameOpacity += 0.05f;
            if (frameOpacity >= 1.0f) {
                setOpacity(1.0f);
                ((Timer) e.getSource()).stop();
            } else {
                setOpacity(frameOpacity);
            }
        });
        timer.start();
    }

    private void styleLargeField(JTextField field) {
        field.setMaximumSize(new Dimension(320, 45));
        field.setPreferredSize(new Dimension(320, 45));
        field.setBackground(FIELD_BG);
        field.setForeground(Color.WHITE);
        field.setCaretColor(ACCENT_PURPLE);
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80), 1),
                new EmptyBorder(0, 15, 0, 15)
        ));
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(170, 170, 170));
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 8, 0));
        return label;
    }

    private void showThemedError(String msg) {
        UIManager.put("OptionPane.background", BG_DARK);
        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void enableDragging(JPanel panel) {
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { initialClick = e.getPoint(); }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(getLocation().x + e.getX() - initialClick.x,
                        getLocation().y + e.getY() - initialClick.y);
            }
        });
    }
}