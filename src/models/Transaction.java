package models;

import java.sql.Timestamp;

public class Transaction {
    private int id;
    private String accountNumber;
    private String type; // DEPOSIT or WITHDRAWAL
    private double amount;
    private Timestamp timestamp;

    public Transaction(int id, String accountNumber, String type, double amount, Timestamp timestamp) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // Getters for displaying history later in the UI
    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public Timestamp getTimestamp() { return timestamp; }
}