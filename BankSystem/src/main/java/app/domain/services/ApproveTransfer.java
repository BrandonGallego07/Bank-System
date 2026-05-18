package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Account;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.enums.TransferStatus;
import app.domain.enums.UserRole;
import app.domain.ports.out.IAccountRepository;
import app.domain.ports.out.ITransferRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ApproveTransfer {

    private final ITransferRepository transferRepository;
    private final IAccountRepository accountRepository;

    public ApproveTransfer(ITransferRepository transferRepository, IAccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    public void approveTransfer(User requester, Long transferId) throws BusinessException {
        if (requester.getRole() != UserRole.BUSINESS_SUPERVISOR)
            throw new BusinessException("Acceso denegado: solo el Supervisor de Empresa puede aprobar transferencias");

        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new BusinessException("Transferencia no encontrada"));

        if (transfer.getStatus() != TransferStatus.WAITING_FOR_APPROVAL)
            throw new BusinessException("La transferencia no esta en espera de aprobacion");

        LocalDateTime created = LocalDateTime.parse(transfer.getCreationDateTime());
        if (LocalDateTime.now().isAfter(created.plusMinutes(60))) {
            transfer.setStatus(TransferStatus.EXPIRED);
            transferRepository.save(transfer);
            throw new BusinessException("La transferencia vencio por falta de aprobacion a tiempo");
        }

        Account origin = accountRepository.findByAccountNumber(transfer.getSourceAccountNumber())
                .orElseThrow(() -> new BusinessException("Cuenta origen no encontrada"));

        if (origin.getBalance() < transfer.getAmount())
            throw new BusinessException("Saldo insuficiente en la cuenta origen");

        Account destination = accountRepository.findByAccountNumber(transfer.getTargetAccountNumber())
                .orElseThrow(() -> new BusinessException("Cuenta destino no encontrada"));

        origin.setBalance(origin.getBalance() - transfer.getAmount());
        destination.setBalance(destination.getBalance() + transfer.getAmount());
        transfer.setStatus(TransferStatus.EXECUTED);
        transfer.setApprovedByUserId(requester.getId());
        transfer.setApprovalDateTime(LocalDateTime.now().toString());

        accountRepository.save(origin);
        accountRepository.save(destination);
        transferRepository.save(transfer);
    }
}
