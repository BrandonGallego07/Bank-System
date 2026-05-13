package domain.ports;

import domain.model.Account;
import domain.model.User;
import java.util.List;

public interface AccountPort {
    Account findByNumber(String accountNumber);
    List<Account> findByCustomerId(String customerId);
    User findUserByCustomerId(String customerId);
    void saveAccount(Account account);
    void updateAccount(Account account);
}
