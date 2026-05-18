package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Account;
import app.domain.models.User;
import app.domain.enums.AccountStatus;
import app.domain.enums.UserRole;
import app.domain.ports.out.IAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class Deposit {

    private final IAccountRepository accountRepository;

    public Deposit(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void deposit(User requester, String accountNumber, double amount) throws BusinessException {
        if (requester.getRole() != UserRole.TELLER_EMPLOYEE)
            throw new BusinessException("Acceso denegado: solo el cajero puede realizar depositos");

        if (amount <= 0)
            throw new BusinessException("El monto del deposito debe ser mayor a cero");

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada"));

        if (account.getStatus() != AccountStatus.ACTIVE)
            throw new BusinessException("La cuenta no esta activa");

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }
}
