package domain.ports;

import domain.model.AuditLog;
import java.util.List;

public interface AuditPort {
    void saveLog(AuditLog log);
    List<AuditLog> findByProductId(String productId);
    List<AuditLog> findAll();
}
