package domain.services;

import domain.model.AuditLog;
import domain.model.User;
import domain.ports.AuditPort;
import domain.enums.UserRole;

import java.util.List;

public class AuditService {

    private final AuditPort auditPort;

    public AuditService(AuditPort auditPort) {
        this.auditPort = auditPort;
    }

    // ─── 1. GUARDAR REGISTRO EN BITACORA ──────────────────────────────────────
    public void saveAuditLog(AuditLog log) {
        if (log.getOperationType() == null || log.getOperationType().isEmpty()) {
            throw new RuntimeException("El tipo de operacion es obligatorio en la bitacora");
        }
        if (log.getExecutedBy() == null) {
            throw new RuntimeException("El usuario ejecutor es obligatorio en la bitacora");
        }
        auditPort.saveLog(log);
    }

    // ─── 2. CONSULTAR BITACORA POR PRODUCTO ───────────────────────────────────
    public List<AuditLog> getAuditLogsByProduct(User requester, String productId) {
        // Clientes solo ven sus propios registros
        if (requester.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            requester.getRole() == UserRole.BUSINESS_CUSTOMER) {
            if (!productId.contains(requester.getRelatedId())) {
                throw new RuntimeException("Acceso denegado: solo puede consultar su propia bitacora");
            }
        }
        // Empleado de ventanilla no tiene acceso a la bitacora
        if (requester.getRole() == UserRole.TELLER_EMPLOYEE) {
            throw new RuntimeException("Acceso denegado: no tiene permisos para consultar la bitacora");
        }
        return auditPort.findByProductId(productId);
    }

    // ─── 3. CONSULTAR BITACORA COMPLETA (solo Analista Interno) ───────────────
    public List<AuditLog> getFullAuditLog(User requester) {
        if (requester.getRole() != UserRole.INTERNAL_ANALYST) {
            throw new RuntimeException("Acceso denegado: solo el Analista Interno puede ver la bitacora completa");
        }
        return auditPort.findAll();
    }
}
