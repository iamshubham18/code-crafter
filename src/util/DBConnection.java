package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // Replace 'your_password' with the password you set in MySQL Workbench
    private static final String URL = "jdbc:mysql://localhost:3306/atm_db";
    private static final String USER = "shubham";
    private static final String PASSWORD = "nisha@sql";

    public static Connection getConnection() {
        try {
            // This matches the connector version 9.5 you just installed
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
            return null;
        }
    }
}