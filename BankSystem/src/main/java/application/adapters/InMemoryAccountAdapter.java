package application.adapters;

import domain.model.Account;
import domain.model.User;
import domain.ports.AccountPort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryAccountAdapter implements AccountPort {

    private final List<Account> accounts = new ArrayList<>();
    private final InMemoryUserAdapter userAdapter;

    public InMemoryAccountAdapter(InMemoryUserAdapter userAdapter) {
        this.userAdapter = userAdapter;
    }

    @Override
    public Account findByNumber(String accountNumber) {
        return accounts.stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada: " + accountNumber));
    }

    @Override
    public List<Account> findByCustomerId(String customerId) {
        return accounts.stream()
                .filter(a -> a.getHolder().getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public User findUserByCustomerId(String customerId) {
        return userAdapter.findByIdentificationId(customerId);
    }

    @Override
    public void saveAccount(Account account) {
        accounts.removeIf(a -> a.getAccountNumber().equals(account.getAccountNumber()));
        accounts.add(account);
    }

    @Override
    public void updateAccount(Account account) {
        saveAccount(account);
    }
}
