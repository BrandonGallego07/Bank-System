package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.enums.TransferStatus;
import app.domain.enums.UserRole;
import app.domain.ports.out.ITransferRepository;
import org.springframework.stereotype.Service;

@Service
public class RejectTransfer {

    private final ITransferRepository transferRepository;

    public RejectTransfer(ITransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public void rejectTransfer(User requester, Long transferId) throws BusinessException {
        if (requester.getRole() != UserRole.BUSINESS_SUPERVISOR)
            throw new BusinessException("Acceso denegado: solo el Supervisor puede rechazar transferencias");

        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new BusinessException("Transferencia no encontrada"));

        if (transfer.getStatus() != TransferStatus.WAITING_FOR_APPROVAL)
            throw new BusinessException("La transferencia no esta en espera de aprobacion");

        transfer.setStatus(TransferStatus.REJECTED);
        transferRepository.save(transfer);
    }
}
