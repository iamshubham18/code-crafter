package ui;

import models.User;
import services.AccountService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame implements RefreshListener {
    private User currentUser;
    private AccountService accountService = new AccountService();
    private JLabel balLabel;
    private JButton toggleBtn;
    private boolean isBalanceVisible = false;
    private Point initialClick;

    // Palette
    private final Color BG_COLOR = new Color(18, 18, 18);
    private final Color CARD_COLOR = new Color(30, 30, 38);
    private final Color ACCENT_PURPLE = new Color(187, 134, 252);

    public Dashboard(User user) {
        this.currentUser = user;

        setUndecorated(true);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 50, 30, 50));
        setContentPane(mainPanel);

        // --- Header ---
        setupHeader(mainPanel);

        // --- Center Content ---
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setOpaque(false);

        // Balance Card
        setupBalanceCard(centerContainer);

        // Action Grid
        setupActionGrid(centerContainer);

        mainPanel.add(centerContainer, BorderLayout.CENTER);
        enableDragging();
    }

    private void setupHeader(JPanel mainPanel) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel welcome = new JLabel("Welcome, " + currentUser.getUsername());
        welcome.setFont(new Font("SansSerif", Font.PLAIN, 18));
        welcome.setForeground(Color.LIGHT_GRAY);

        JButton closeBtn = new JButton("✕");
        closeBtn.setForeground(new Color(207, 102, 121));
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> System.exit(0));

        header.add(welcome, BorderLayout.WEST);
        header.add(closeBtn, BorderLayout.EAST);
        mainPanel.add(header, BorderLayout.NORTH);
    }

    private void setupBalanceCard(JPanel container) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(new EmptyBorder(30, 30, 30, 30));
        card.setMaximumSize(new Dimension(800, 180));

        balLabel = new JLabel("••••••••");
        balLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        balLabel.setForeground(Color.WHITE);
        balLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        toggleBtn = new JButton("SHOW BALANCE");
        toggleBtn.setForeground(ACCENT_PURPLE);
        toggleBtn.setContentAreaFilled(false);
        toggleBtn.setBorder(BorderFactory.createLineBorder(ACCENT_PURPLE, 1));
        toggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggleBtn.addActionListener(e -> toggleBalance());

        card.add(balLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(toggleBtn);

        container.add(Box.createVerticalGlue());
        container.add(card);
        container.add(Box.createVerticalStrut(40));
    }

    private void toggleBalance() {
        if (isBalanceVisible) {
            balLabel.setText("••••••••");
            toggleBtn.setText("SHOW BALANCE");
        } else {
            double bal = accountService.getBalance(currentUser.getUserId());
            balLabel.setText("₹" + String.format("%.2f", bal));
            toggleBtn.setText("HIDE BALANCE");
        }
        isBalanceVisible = !isBalanceVisible;
    }

    private void setupActionGrid(JPanel container) {
        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setOpaque(false);

        grid.add(createAnimatedBtn("Deposit", new Color(3, 218, 198),
                e -> new DepositScreen(currentUser, this).setVisible(true)));

        grid.add(createAnimatedBtn("Withdraw", new Color(207, 102, 121),
                e -> new WithdrawScreen(currentUser, this).setVisible(true)));

        grid.add(createAnimatedBtn("History", CARD_COLOR,
                e -> new TransactionHistoryScreen(currentUser).setVisible(true)));

        // UPDATED: Changed from opening LoginScreen to System.exit(0)
        grid.add(createAnimatedBtn("Logout & Exit", new Color(45, 45, 55), e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout and exit the ATM?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }));

        container.add(grid);
        container.add(Box.createVerticalGlue());
    }

    @Override
    public void onTransactionComplete() {
        if (isBalanceVisible) {
            double bal = accountService.getBalance(currentUser.getUserId());
            balLabel.setText("₹" + String.format("%.2f", bal));
        }
    }

    private JButton createAnimatedBtn(String text, Color baseColor, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setBackground(baseColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    private void enableDragging() {
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { initialClick = e.getPoint(); }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(getLocation().x + e.getX() - initialClick.x, getLocation().y + e.getY() - initialClick.y);
            }
        });
    }
}