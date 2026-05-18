package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Account;
import app.domain.models.User;
import app.domain.enums.AccountStatus;
import app.domain.enums.UserRole;
import app.domain.ports.out.IAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class CancelAccount {

    private final IAccountRepository accountRepository;

    public CancelAccount(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void cancelAccount(User requester, String accountNumber) throws BusinessException {
        if (requester.getRole() != UserRole.INTERNAL_ANALYST)
            throw new BusinessException("Acceso denegado: solo el Analista Interno puede cancelar cuentas");

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada"));

        account.setStatus(AccountStatus.CANCELED);
        accountRepository.save(account);
    }
}
