package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Account;
import app.domain.models.User;
import app.domain.enums.AccountStatus;
import app.domain.enums.UserRole;
import app.domain.enums.UserStatus;
import app.domain.ports.out.IAccountRepository;
import app.domain.ports.out.IUserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class CreateAccountForCustomer {

    private final IAccountRepository accountRepository;
    private final IUserRepository userRepository;

    public CreateAccountForCustomer(IAccountRepository accountRepository, IUserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account create(User requester, Account account, String customerId) throws BusinessException {
        if (requester.getRole() != UserRole.COMMERCIAL_EMPLOYEE)
            throw new BusinessException("Acceso denegado: solo el Empleado Comercial puede crear cuentas para clientes");

        User customer = userRepository.findByIdentificationId(customerId)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        if (customer.getStatus() == UserStatus.INACTIVE || customer.getStatus() == UserStatus.BLOCKED)
            throw new BusinessException("No se puede crear cuenta a un cliente inactivo o bloqueado");

        account.setHolderId(customerId);
        account.setStatus(AccountStatus.ACTIVE);
        account.setOpeningDate(LocalDateTime.now().toString());
        return accountRepository.save(account);
    }
}
