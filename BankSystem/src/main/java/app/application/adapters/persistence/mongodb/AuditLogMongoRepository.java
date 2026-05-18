package app.application.adapters.persistence.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AuditLogMongoRepository extends MongoRepository<AuditLogDocument, String> {
    List<AuditLogDocument> findByAffectedProductId(String productId);
}
