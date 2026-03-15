package bank_model;

import bank_enums.AccountStatus;
import bank_enums.AccountType;
import bank_enums.CurrencyType;

public class Account {

    private String accountNumber;
    private AccountType accountType;
    private Customer holder;
    private double balance;
    private CurrencyType currency;
    private AccountStatus status;
    private String openingDate;

    public Account() {
    }

    public Account(String accountNumber, AccountType accountType, Customer holder, double balance,
                   CurrencyType currency, AccountStatus status, String openingDate) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.holder = holder;
        this.balance = balance;
        this.currency = currency;
        this.status = status;
        this.openingDate = openingDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Customer getHolder() {
        return holder;
    }

    public void setHolder(Customer holder) {
        this.holder = holder;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType +
                ", holder=" + holder +
                ", balance=" + balance +
                ", currency=" + currency +
                ", status=" + status +
                ", openingDate='" + openingDate + '\'' +
                '}';
    }
}