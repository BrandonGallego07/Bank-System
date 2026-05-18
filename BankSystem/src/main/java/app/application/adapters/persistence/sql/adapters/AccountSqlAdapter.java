package app.application.adapters.persistence.sql.adapters;

import app.application.adapters.persistence.sql.entities.AccountEntity;
import app.application.adapters.persistence.sql.repositories.AccountJpaRepository;
import app.domain.models.Account;
import app.domain.ports.out.IAccountRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AccountSqlAdapter implements IAccountRepository {

    private final AccountJpaRepository jpaRepository;

    public AccountSqlAdapter(AccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private Account toModel(AccountEntity e) {
        Account a = new Account();
        a.setId(e.getId());
        a.setAccountNumber(e.getAccountNumber());
        a.setAccountType(e.getAccountType());
        a.setHolderId(e.getHolderId());
        a.setBalance(e.getBalance());
        a.setCurrency(e.getCurrency());
        a.setStatus(e.getStatus());
        a.setOpeningDate(e.getOpeningDate());
        return a;
    }

    private AccountEntity toEntity(Account a) {
        AccountEntity e = new AccountEntity();
        e.setId(a.getId());
        e.setAccountNumber(a.getAccountNumber());
        e.setAccountType(a.getAccountType());
        e.setHolderId(a.getHolderId());
        e.setBalance(a.getBalance());
        e.setCurrency(a.getCurrency());
        e.setStatus(a.getStatus());
        e.setOpeningDate(a.getOpeningDate());
        return e;
    }

    @Override
    public Account save(Account account) {
        return toModel(jpaRepository.save(toEntity(account)));
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return jpaRepository.findByAccountNumber(accountNumber).map(this::toModel);
    }

    @Override
    public List<Account> findByHolderId(String holderId) {
        return jpaRepository.findByHolderId(holderId).stream().map(this::toModel).collect(Collectors.toList());
    }
}
