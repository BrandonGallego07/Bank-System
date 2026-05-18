package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransferJpaRepository extends JpaRepository<TransferEntity, Long> {
    List<TransferEntity> findBySourceAccountNumberOrTargetAccountNumber(String source, String target);
}
