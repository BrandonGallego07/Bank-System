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
public class OpenAccount {

    private final IAccountRepository accountRepository;
    private final IUserRepository userRepository;

    public OpenAccount(IAccountRepository accountRepository, IUserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account openAccount(User requester, Account account) throws BusinessException {
        if (requester.getRole() != UserRole.TELLER_EMPLOYEE &&
            requester.getRole() != UserRole.COMMERCIAL_EMPLOYEE &&
            requester.getRole() != UserRole.INTERNAL_ANALYST)
            throw new BusinessException("Acceso denegado: no tiene permisos para abrir cuentas");

        User holder = userRepository.findByIdentificationId(account.getHolderId())
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        if (holder.getStatus() == UserStatus.INACTIVE || holder.getStatus() == UserStatus.BLOCKED)
            throw new BusinessException("No se puede abrir cuenta a un cliente inactivo o bloqueado");

        account.setStatus(AccountStatus.ACTIVE);
        account.setOpeningDate(LocalDateTime.now().toString());
        return accountRepository.save(account);
    }
}
