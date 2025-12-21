package models;

public class User {
    private int userId;
    private String username;
    private String pin;

    public User(int userId, String username, String pin) {
        this.userId = userId;
        this.username = username;
        this.pin = pin;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPin() { return pin; }
}