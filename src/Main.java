import ui.Dashboard;
import models.User;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            // BYPASS LOGIN: Create a dummy user for testing the UI
            // Assuming User constructor is: User(id, username, pin)
            User testUser = new User(1, "TestUser", "1234");

            // Launch Dashboard directly
            new Dashboard(testUser).setVisible(true);

            System.out.println("Testing Mode: Login Bypassed.");
        });
    }
}