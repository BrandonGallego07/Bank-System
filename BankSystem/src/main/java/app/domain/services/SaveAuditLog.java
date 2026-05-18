package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.AuditLog;
import app.domain.ports.out.IAuditRepository;
import org.springframework.stereotype.Service;

@Service
public class SaveAuditLog {

    private final IAuditRepository auditRepository;

    public SaveAuditLog(IAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public void save(AuditLog log) throws BusinessException {
        if (log.getOperationType() == null || log.getOperationType().isEmpty())
            throw new BusinessException("El tipo de operacion es obligatorio en la bitacora");

        auditRepository.save(log);
    }
}
