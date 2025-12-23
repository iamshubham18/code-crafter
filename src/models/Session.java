package models;

public class Session {
    private static User currentUser;
    private static Account currentAccount;

    public static void login(User user, Account account) {
        currentUser = user;
        currentAccount = account;
    }

    public static void logout() {
        currentUser = null;
        currentAccount = null;
    }

    public static User getUser() { return currentUser; }
    public static Account getAccount() { return currentAccount; }
}