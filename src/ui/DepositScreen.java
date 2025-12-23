package ui;

import models.User;
import services.AccountService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class DepositScreen extends JFrame {
    private AccountService accountService = new AccountService();
    private RefreshListener listener; // 1. ADDED: Reference for the Dashboard notification

    // Theme Palette
    private final Color BG_COLOR = new Color(25, 25, 35);
    private final Color ACCENT_TEAL = new Color(3, 218, 198);
    private final Color CARD_COLOR = new Color(35, 35, 45);

    // 2. UPDATED: Constructor now accepts RefreshListener
    public DepositScreen(User user, RefreshListener listener) {
        this.listener = listener; // Store the listener reference

        setUndecorated(true);
        setSize(400, 350);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createLineBorder(ACCENT_TEAL, 1));
        setContentPane(mainPanel);

        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(15, 20, 0, 15));

        JLabel title = new JLabel("DEPOSIT FUNDS");
        title.setFont(new Font("SansSerif", Font.BOLD, 14));
        title.setForeground(ACCENT_TEAL);

        JButton closeBtn = new JButton("✕");
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        prompt.setForeground(Color.WHITE);
        prompt.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        contentPanel.add(prompt, gbc);

        JTextField amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(280, 50));
        amountField.setBackground(CARD_COLOR);
        amountField.setForeground(ACCENT_TEAL);
        amountField.setCaretColor(Color.WHITE);
        amountField.setFont(new Font("Monospaced", Font.BOLD, 24));
        amountField.setHorizontalAlignment(JTextField.CENTER);
        amountField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        gbc.gridy = 1;
        contentPanel.add(amountField, gbc);

        JButton confirmBtn = new JButton("CONFIRM DEPOSIT");
        confirmBtn.setPreferredSize(new Dimension(280, 50));
        confirmBtn.setBackground(ACCENT_TEAL);
        confirmBtn.setForeground(Color.BLACK);
        confirmBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        confirmBtn.setFocusPainted(false);
        confirmBtn.setBorderPainted(false);
        confirmBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 2;
        gbc.insets = new Insets(25, 0, 0, 0);
        contentPanel.add(confirmBtn, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // --- Dragging Logic ---
        final Point[] initialClick = new Point[1];
        mainPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { initialClick[0] = e.getPoint(); }
        });
        mainPanel.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(getLocation().x + e.getX() - initialClick[0].x,
                        getLocation().y + e.getY() - initialClick[0].y);
            }
        });

        // --- Action Logic ---
        confirmBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    showThemedMessage("Enter an amount greater than 0.", "Invalid Input");
                    return;
                }

                if (accountService.deposit(user.getUserId(), amount)) {
                    showThemedMessage("Success! ₹" + amount + " deposited.", "Success");

                    // 3. ADDED: Notify Dashboard to refresh the rolling balance
                    if (listener != null) {
                        listener.onTransactionComplete();
                    }

                    dispose();
                } else {
                    showThemedMessage("Transaction Failed.", "Error");
                }
            } catch (NumberFormatException ex) {
                showThemedMessage("Please enter a valid number.", "Input Error");
            }
        });
    }

    private void showThemedMessage(String message, String title) {
        UIManager.put("OptionPane.background", BG_COLOR);
        UIManager.put("Panel.background", BG_COLOR);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", CARD_COLOR);
        UIManager.put("Button.foreground", ACCENT_TEAL);
        UIManager.put("Button.border", BorderFactory.createLineBorder(ACCENT_TEAL, 1));

        JLabel label = new JLabel(message);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JOptionPane.showMessageDialog(this, label, title, JOptionPane.PLAIN_MESSAGE);
    }
}