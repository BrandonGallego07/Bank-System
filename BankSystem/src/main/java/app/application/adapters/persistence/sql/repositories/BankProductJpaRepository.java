package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.BankProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BankProductJpaRepository extends JpaRepository<BankProductEntity, Long> {
    Optional<BankProductEntity> findByProductCode(String productCode);
    boolean existsByProductCode(String productCode);
}
