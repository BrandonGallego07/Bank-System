package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Account;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.out.IAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAccountByNumber {

    private final IAccountRepository accountRepository;

    public GetAccountByNumber(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(User requester, String accountNumber) throws BusinessException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada"));

        if (requester.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            requester.getRole() == UserRole.BUSINESS_EMPLOYEE) {
            if (!account.getHolderId().equals(requester.getIdentificationId()))
                throw new BusinessException("Acceso denegado: no puede ver cuentas de otros clientes");
        }

        return account;
    }
}
