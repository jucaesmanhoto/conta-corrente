package models;

public class Account {
    private int balance = 0;
    private String accountNumber;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void deposit(int valueInCents) {
        balance += valueInCents;
    }

    public void withdraw(int valueInCents) {
        if (valueInCents > balance)
            throw new RuntimeException("Insufficient balance.");
        balance -= valueInCents;
    }
}
