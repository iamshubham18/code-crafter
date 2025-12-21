package ui;

import models.User;
import services.AccountService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Dashboard extends JFrame {
    private User currentUser;
    private AccountService accountService = new AccountService();
    private boolean isBalanceVisible = false;

    public Dashboard(User user) {
        this.currentUser = user;

        // 1. Window Setup
        setTitle("GDB Glass Portal");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // We use a custom JPanel to draw a colorful gradient background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                // Creating a beautiful Blue to Purple Gradient
                GradientPaint gp = new GradientPaint(0, 0, new Color(41, 128, 185),
                        getWidth(), getHeight(), new Color(142, 68, 173));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        // 2. The Glass Container
        JPanel glassPanel = new JPanel();
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));

        // TRANSPARENCY: The last number (60) is the alpha channel (0-255)
        glassPanel.setBackground(new Color(255, 255, 255, 60));
        glassPanel.setOpaque(false); // Required for custom alpha background
        glassPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // 3. Balance Display (Glassy Card)
        JPanel balanceCard = createGlassCard();
        JLabel balLabel = new JLabel("********");
        balLabel.setFont(new Font("SansSerif", Font.BOLD, 42));
        balLabel.setForeground(Color.WHITE);

        JButton toggleBtn = new JButton("View Balance");
        styleGlassButton(toggleBtn);

        toggleBtn.addActionListener(e -> {
            if (isBalanceVisible) {
                balLabel.setText("********");
                toggleBtn.setText("View Balance");
            } else {
                double bal = accountService.getBalance(user.getUserId());
                balLabel.setText("$" + String.format("%.2f", bal));
                toggleBtn.setText("Hide Balance");
            }
            isBalanceVisible = !isBalanceVisible;
        });

        balanceCard.add(balLabel);
        balanceCard.add(Box.createVerticalStrut(10));
        balanceCard.add(toggleBtn);

        // 4. Action Grid
        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setOpaque(false);
        grid.add(createGlassActionBtn("Deposit", e -> new DepositScreen(currentUser).setVisible(true)));
        grid.add(createGlassActionBtn("Withdraw", e -> new WithdrawScreen(currentUser).setVisible(true)));
        grid.add(createGlassActionBtn("History", e -> new TransactionHistoryScreen(currentUser).setVisible(true)));
        grid.add(createGlassActionBtn("Logout", e -> System.exit(0)));

        glassPanel.add(balanceCard);
        glassPanel.add(Box.createVerticalStrut(40));
        glassPanel.add(grid);

        backgroundPanel.add(glassPanel, BorderLayout.CENTER);
    }

    // Helper to create a "Glass" look for cards
    private JPanel createGlassCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 40)); // Frosted effect
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(new Color(255, 255, 255, 80)); // Border
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        return card;
    }

    private JButton createGlassActionBtn(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    private void styleGlassButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setForeground(new Color(230, 230, 230));
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}