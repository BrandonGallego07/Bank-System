package app.application.adapters.persistence.sql.repositories;

import app.application.adapters.persistence.sql.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanJpaRepository extends JpaRepository<LoanEntity, Long> {
    List<LoanEntity> findByApplicantId(String applicantId);
}
