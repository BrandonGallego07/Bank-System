package app.domain.models;

import app.domain.enums.AccountStatus;
import app.domain.enums.AccountType;
import app.domain.enums.CurrencyType;

public class Account {
    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private String holderId;
    private double balance;
    private CurrencyType currency;
    private AccountStatus status;
    private String openingDate;

    public Account() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    public String getHolderId() { return holderId; }
    public void setHolderId(String holderId) { this.holderId = holderId; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public CurrencyType getCurrency() { return currency; }
    public void setCurrency(CurrencyType currency) { this.currency = currency; }
    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }
    public String getOpeningDate() { return openingDate; }
    public void setOpeningDate(String openingDate) { this.openingDate = openingDate; }
}
