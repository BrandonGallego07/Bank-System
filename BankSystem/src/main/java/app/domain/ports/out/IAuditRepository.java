package app.domain.ports.out;
import app.domain.models.AuditLog;
import java.util.List;
public interface IAuditRepository {
    void save(AuditLog log);
    List<AuditLog> findAll();
    List<AuditLog> findByProductId(String productId);
}
