package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.AuditLog;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.out.IAuditRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GetAuditLogsByProduct {

    private final IAuditRepository auditRepository;

    public GetAuditLogsByProduct(IAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public List<AuditLog> getLogs(User requester, String productId) throws BusinessException {
        if (requester.getRole() == UserRole.TELLER_EMPLOYEE)
            throw new BusinessException("Acceso denegado: no tiene permisos para consultar la bitacora");

        return auditRepository.findByProductId(productId);
    }
}
