package app.domain.ports.out;
import app.domain.models.Account;
import java.util.List;
import java.util.Optional;
public interface IAccountRepository {
    Account save(Account account);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByHolderId(String holderId);
}
