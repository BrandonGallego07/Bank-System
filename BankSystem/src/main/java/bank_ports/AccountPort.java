package bank_ports;

import bank_model.Account;

public interface AccountPort {
    Account findByNumber(String accountNumber);
    void updateAccount(Account account);
}
