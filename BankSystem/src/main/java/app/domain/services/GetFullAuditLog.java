package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.AuditLog;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.out.IAuditRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GetFullAuditLog {

    private final IAuditRepository auditRepository;

    public GetFullAuditLog(IAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public List<AuditLog> getAll(User requester) throws BusinessException {
        if (requester.getRole() != UserRole.INTERNAL_ANALYST)
            throw new BusinessException("Acceso denegado: solo el Analista Interno puede ver la bitacora completa");

        return auditRepository.findAll();
    }
}
