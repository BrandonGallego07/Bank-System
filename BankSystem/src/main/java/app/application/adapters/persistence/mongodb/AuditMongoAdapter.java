package app.application.adapters.persistence.mongodb;

import app.domain.models.AuditLog;
import app.domain.ports.out.IAuditRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuditMongoAdapter implements IAuditRepository {

    private final AuditLogMongoRepository mongoRepository;

    public AuditMongoAdapter(AuditLogMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    private AuditLog toModel(AuditLogDocument d) {
        AuditLog log = new AuditLog();
        log.setId(d.getId());
        log.setOperationType(d.getOperationType());
        log.setOperationDateTime(d.getOperationDateTime());
        log.setUserId(d.getUserId());
        log.setUserRole(d.getUserRole());
        log.setAffectedProductId(d.getAffectedProductId());
        log.setDetailData(d.getDetailData());
        return log;
    }

    private AuditLogDocument toDocument(AuditLog log) {
        AuditLogDocument d = new AuditLogDocument();
        d.setId(log.getId());
        d.setOperationType(log.getOperationType());
        d.setOperationDateTime(log.getOperationDateTime());
        d.setUserId(log.getUserId());
        d.setUserRole(log.getUserRole());
        d.setAffectedProductId(log.getAffectedProductId());
        d.setDetailData(log.getDetailData());
        return d;
    }

    @Override
    public void save(AuditLog log) {
        mongoRepository.save(toDocument(log));
    }

    @Override
    public List<AuditLog> findAll() {
        return mongoRepository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByProductId(String productId) {
        return mongoRepository.findByAffectedProductId(productId).stream().map(this::toModel).collect(Collectors.toList());
    }
}
