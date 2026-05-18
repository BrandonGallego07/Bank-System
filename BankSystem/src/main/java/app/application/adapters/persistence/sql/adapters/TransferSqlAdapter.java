package app.application.adapters.persistence.sql.adapters;

import app.application.adapters.persistence.sql.entities.TransferEntity;
import app.application.adapters.persistence.sql.repositories.TransferJpaRepository;
import app.domain.models.Transfer;
import app.domain.ports.out.ITransferRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TransferSqlAdapter implements ITransferRepository {

    private final TransferJpaRepository jpaRepository;

    public TransferSqlAdapter(TransferJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private Transfer toModel(TransferEntity e) {
        Transfer t = new Transfer();
        t.setId(e.getId());
        t.setSourceAccountNumber(e.getSourceAccountNumber());
        t.setTargetAccountNumber(e.getTargetAccountNumber());
        t.setAmount(e.getAmount());
        t.setCreationDateTime(e.getCreationDateTime());
        t.setApprovalDateTime(e.getApprovalDateTime());
        t.setStatus(e.getStatus());
        t.setCreatedByUserId(e.getCreatedByUserId());
        t.setApprovedByUserId(e.getApprovedByUserId());
        return t;
    }

    private TransferEntity toEntity(Transfer t) {
        TransferEntity e = new TransferEntity();
        e.setId(t.getId());
        e.setSourceAccountNumber(t.getSourceAccountNumber());
        e.setTargetAccountNumber(t.getTargetAccountNumber());
        e.setAmount(t.getAmount());
        e.setCreationDateTime(t.getCreationDateTime());
        e.setApprovalDateTime(t.getApprovalDateTime());
        e.setStatus(t.getStatus());
        e.setCreatedByUserId(t.getCreatedByUserId());
        e.setApprovedByUserId(t.getApprovedByUserId());
        return e;
    }

    @Override
    public Transfer save(Transfer transfer) {
        return toModel(jpaRepository.save(toEntity(transfer)));
    }

    @Override
    public Optional<Transfer> findById(Long id) {
        return jpaRepository.findById(id).map(this::toModel);
    }

    @Override
    public List<Transfer> findByAccountNumber(String accountNumber) {
        return jpaRepository.findBySourceAccountNumberOrTargetAccountNumber(accountNumber, accountNumber)
                .stream().map(this::toModel).collect(Collectors.toList());
    }
}
