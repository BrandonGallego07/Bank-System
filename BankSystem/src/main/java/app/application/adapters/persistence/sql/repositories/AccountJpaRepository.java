package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
    List<AccountEntity> findByHolderId(String holderId);
}
