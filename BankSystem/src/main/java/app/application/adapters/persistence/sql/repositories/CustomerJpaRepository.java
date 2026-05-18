package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByCustomerId(String customerId);
    boolean existsByCustomerId(String customerId);
}
