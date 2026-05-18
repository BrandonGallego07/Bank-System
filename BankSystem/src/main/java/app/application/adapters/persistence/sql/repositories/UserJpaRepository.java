package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByIdentificationId(String identificationId);
    boolean existsByIdentificationId(String identificationId);
}
