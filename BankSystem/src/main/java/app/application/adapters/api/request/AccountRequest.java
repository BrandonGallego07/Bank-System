package app.application.adapters.api.request;

import app.domain.enums.AccountType;
import app.domain.enums.CurrencyType;

public class AccountRequest {
    private String accountNumber;
    private AccountType accountType;
    private String holderId;
    private double balance;
    private CurrencyType currency;

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
}
