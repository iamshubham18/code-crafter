

import ui.LoginScreen;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // We avoid System LookAndFeel to keep our custom Dark Theme consistent
        SwingUtilities.invokeLater(() -> {
            // Start with the Login Screen instead of bypassing it
            new LoginScreen().setVisible(true);

            System.out.println("Application Started: Awaiting Login...");
        });
    }
}