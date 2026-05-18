package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Account;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.out.IAccountRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GetAccountsByCustomer {

    private final IAccountRepository accountRepository;

    public GetAccountsByCustomer(IAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAccounts(User requester, String customerId) throws BusinessException {
        if (requester.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            requester.getRole() == UserRole.BUSINESS_CUSTOMER) {
            if (!requester.getIdentificationId().equals(customerId))
                throw new BusinessException("Acceso denegado: solo puede ver sus propias cuentas");
        }

        return accountRepository.findByHolderId(customerId);
    }
}
