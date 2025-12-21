package models;

public class Account {
    private String accountNumber;
    private int userId;
    private double balance;

    public Account(String accountNumber, int userId, double balance) {
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.balance = balance;
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
}