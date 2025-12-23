package ui;

import models.User;
import services.TransactionService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

public class TransactionHistoryScreen extends JFrame {
    private TransactionService txService = new TransactionService();

    // Theme Palette
    private final Color BG_COLOR = new Color(18, 18, 18);
    private final Color CARD_COLOR = new Color(30, 30, 38);
    private final Color ACCENT_PURPLE = new Color(187, 134, 252);
    private final Color TEAL = new Color(3, 218, 198);
    private final Color ROSE = new Color(207, 102, 121);

    public TransactionHistoryScreen(User user) {
        setUndecorated(true); // Immersive look
        setSize(700, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createLineBorder(ACCENT_PURPLE, 1));
        setContentPane(mainPanel);

        // --- Custom Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("TRANSACTION LOG (₹)");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(ACCENT_PURPLE);

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

        // --- Table Setup ---
        String[] columnNames = {"Type", "Amount (₹)", "Date & Time"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Make table read-only
        };

        JTable table = new JTable(model);
        styleTable(table);

        // Load Data
        try {
            ResultSet rs = txService.getTransactionsByUserId(user.getUserId());
            while (rs != null && rs.next()) {
                String type = rs.getString("type").toUpperCase();
                double amount = rs.getDouble("amount");
                String timestamp = rs.getTimestamp("timestamp").toString();
                model.addRow(new Object[]{type, amount, timestamp});
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        // --- ScrollPane Styling ---
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_COLOR);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add Dragging Logic
        enableDragging(mainPanel);
    }

    private void styleTable(JTable table) {
        table.setBackground(BG_COLOR);
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(50, 50, 50));
        table.setRowHeight(40);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(187, 134, 252, 40));

        // Style Header
        JTableHeader header = table.getTableHeader();
        header.setBackground(CARD_COLOR);
        header.setForeground(ACCENT_PURPLE);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ACCENT_PURPLE));

        // Custom Renderer for Row Colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                c.setBackground(row % 2 == 0 ? BG_COLOR : CARD_COLOR); // Zebra striping

                String type = t.getValueAt(row, 0).toString();
                if (col == 1) { // Amount Column
                    setForeground(type.contains("DEPOSIT") ? TEAL : ROSE);
                    setFont(new Font("Monospaced", Font.BOLD, 14));
                } else {
                    setForeground(Color.WHITE);
                }

                setBorder(noFocusBorder);
                return c;
            }
        });
    }

    private void enableDragging(JPanel panel) {
        final Point[] initialClick = new Point[1];
        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { initialClick[0] = e.getPoint(); }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(getLocation().x + e.getX() - initialClick[0].x,
                        getLocation().y + e.getY() - initialClick[0].y);
            }
        });
    }
}