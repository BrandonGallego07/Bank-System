package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Transfer;
import app.domain.enums.TransferStatus;
import app.domain.ports.out.ITransferRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ExpireTransfer {

    private final ITransferRepository transferRepository;

    public ExpireTransfer(ITransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public void expireIfOverdue(Long transferId) throws BusinessException {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new BusinessException("Transferencia no encontrada"));

        if (transfer.getStatus() != TransferStatus.WAITING_FOR_APPROVAL) return;

        LocalDateTime created = LocalDateTime.parse(transfer.getCreationDateTime());
        if (LocalDateTime.now().isAfter(created.plusMinutes(60))) {
            transfer.setStatus(TransferStatus.EXPIRED);
            transferRepository.save(transfer);
        }
    }
}
