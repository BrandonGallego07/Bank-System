package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Account;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.enums.AccountStatus;
import app.domain.enums.TransferStatus;
import app.domain.enums.UserRole;
import app.domain.ports.out.IAccountRepository;
import app.domain.ports.out.ITransferRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class CreateTransfer {

    private static final double HIGH_AMOUNT_THRESHOLD = 10000000.0;

    private final ITransferRepository transferRepository;
    private final IAccountRepository accountRepository;

    public CreateTransfer(ITransferRepository transferRepository, IAccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    public Transfer createTransfer(User requester, Transfer transfer) throws BusinessException {
        if (requester.getRole() != UserRole.NATURAL_PERSON_CUSTOMER &&
            requester.getRole() != UserRole.BUSINESS_EMPLOYEE &&
            requester.getRole() != UserRole.COMMERCIAL_EMPLOYEE)
            throw new BusinessException("Acceso denegado: no tiene permisos para crear transferencias");

        if (transfer.getAmount() <= 0)
            throw new BusinessException("El monto debe ser mayor a cero");

        Account origin = accountRepository.findByAccountNumber(transfer.getSourceAccountNumber())
                .orElseThrow(() -> new BusinessException("Cuenta origen no encontrada"));

        if (origin.getStatus() == AccountStatus.BLOCKED || origin.getStatus() == AccountStatus.CANCELED)
            throw new BusinessException("La cuenta origen esta bloqueada o cancelada");

        transfer.setCreatedByUserId(requester.getId());
        transfer.setCreationDateTime(LocalDateTime.now().toString());

        if (requester.getRole() == UserRole.BUSINESS_EMPLOYEE && transfer.getAmount() > HIGH_AMOUNT_THRESHOLD) {
            transfer.setStatus(TransferStatus.WAITING_FOR_APPROVAL);
        } else {
            if (origin.getBalance() < transfer.getAmount())
                throw new BusinessException("Saldo insuficiente en la cuenta origen");

            Account destination = accountRepository.findByAccountNumber(transfer.getTargetAccountNumber())
                    .orElseThrow(() -> new BusinessException("Cuenta destino no encontrada"));

            origin.setBalance(origin.getBalance() - transfer.getAmount());
            destination.setBalance(destination.getBalance() + transfer.getAmount());
            transfer.setStatus(TransferStatus.EXECUTED);

            accountRepository.save(origin);
            accountRepository.save(destination);
        }

        return transferRepository.save(transfer);
    }
}
