package domain.services;

import domain.model.AuditLog;
import domain.model.Account;
import domain.model.Loan;
import domain.model.User;
import domain.ports.AccountPort;
import domain.ports.LoanPort;
import domain.ports.AuditPort;
import domain.enums.AccountStatus;
import domain.enums.LoanStatus;
import domain.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;

public class LoanService {

    private final LoanPort loanPort;
    private final AccountPort accountPort;
    private final AuditPort auditPort;

    public LoanService(LoanPort loanPort, AccountPort accountPort, AuditPort auditPort) {
        this.loanPort = loanPort;
        this.accountPort = accountPort;
        this.auditPort = auditPort;
    }

    // ─── 1. SOLICITAR PRESTAMO ─────────────────────────────────────────────────
    public Loan requestLoan(User user, Loan loan) {
        if (user.getRole() != UserRole.NATURAL_PERSON_CUSTOMER &&
            user.getRole() != UserRole.BUSINESS_CUSTOMER &&
            user.getRole() != UserRole.COMMERCIAL_EMPLOYEE) {
            throw new RuntimeException("Acceso denegado: no tiene permisos para solicitar prestamos");
        }

        if (loan.getApplicant() == null) {
            throw new RuntimeException("El prestamo debe tener un cliente solicitante");
        }

        if (loan.getRequestedAmount() <= 0) {
            throw new RuntimeException("El monto solicitado debe ser mayor a cero");
        }

        loan.setStatus(LoanStatus.UNDER_REVIEW);
        loanPort.saveLoan(loan);

        AuditLog log = new AuditLog();
        log.setAuditLogId("AUDIT-" + System.currentTimeMillis());
        log.setOperationType("SOLICITUD_PRESTAMO");
        log.setOperationDateTime(LocalDateTime.now().toString());
        log.setExecutedBy(user);
        log.setUserRole(user.getRole().name());
        log.setAffectedProductId(String.valueOf(loan.getLoanId()));
        log.setDetailData("montoSolicitado=" + loan.getRequestedAmount() + ", estado=EN_ESTUDIO");
        auditPort.saveLog(log);

        return loan;
    }

    // ─── 2. APROBAR PRESTAMO ───────────────────────────────────────────────────
    public void approveLoan(User user, int loanId, double approvedAmount) {
        if (user.getRole() != UserRole.INTERNAL_ANALYST) {
            throw new RuntimeException("Acceso denegado: solo el Analista Interno puede aprobar prestamos");
        }

        Loan loan = loanPort.findById(loanId);

        if (loan.getStatus() != LoanStatus.UNDER_REVIEW) {
            throw new RuntimeException("El prestamo no esta en estado En Estudio");
        }

        if (approvedAmount <= 0) {
            throw new RuntimeException("El monto aprobado debe ser mayor a cero");
        }

        String previousStatus = loan.getStatus().name();
        loan.setApprovedAmount(approvedAmount);
        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovalDate(LocalDateTime.now().toString());
        loanPort.saveLoan(loan);

        AuditLog log = new AuditLog();
        log.setAuditLogId("AUDIT-" + System.currentTimeMillis());
        log.setOperationType("APROBACION_PRESTAMO");
        log.setOperationDateTime(LocalDateTime.now().toString());
        log.setExecutedBy(user);
        log.setUserRole(user.getRole().name());
        log.setAffectedProductId(String.valueOf(loanId));
        log.setDetailData(
            "montoAprobado=" + approvedAmount +
            ", tasa=" + loan.getInterestRate() +
            ", estadoAnterior=" + previousStatus +
            ", nuevoEstado=APROBADO" +
            ", analistaAprobador=" + user.getUserId()
        );
        auditPort.saveLog(log);
    }

    // ─── 3. RECHAZAR PRESTAMO ──────────────────────────────────────────────────
    public void rejectLoan(User user, int loanId) {
        if (user.getRole() != UserRole.INTERNAL_ANALYST) {
            throw new RuntimeException("Acceso denegado: solo el Analista Interno puede rechazar prestamos");
        }

        Loan loan = loanPort.findById(loanId);

        if (loan.getStatus() != LoanStatus.UNDER_REVIEW) {
            throw new RuntimeException("El prestamo no esta en estado En Estudio");
        }

        loan.setStatus(LoanStatus.REJECTED);
        loanPort.saveLoan(loan);

        AuditLog log = new AuditLog();
        log.setAuditLogId("AUDIT-" + System.currentTimeMillis());
        log.setOperationType("RECHAZO_PRESTAMO");
        log.setOperationDateTime(LocalDateTime.now().toString());
        log.setExecutedBy(user);
        log.setUserRole(user.getRole().name());
        log.setAffectedProductId(String.valueOf(loanId));
        log.setDetailData("estadoAnterior=EN_ESTUDIO, nuevoEstado=RECHAZADO");
        auditPort.saveLog(log);
    }

    // ─── 4. DESEMBOLSAR PRESTAMO ───────────────────────────────────────────────
    public void disburseLoan(User user, int loanId) {
        if (user.getRole() != UserRole.INTERNAL_ANALYST) {
            throw new RuntimeException("Acceso denegado: solo el Analista Interno puede desembolsar prestamos");
        }

        Loan loan = loanPort.findById(loanId);

        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new RuntimeException("El prestamo debe estar en estado Aprobado para desembolsar");
        }

        if (loan.getDestinationAccount() == null) {
            throw new RuntimeException("Debe definir una cuenta destino para el desembolso");
        }

        Account destination = accountPort.findByNumber(loan.getDestinationAccount().getAccountNumber());

        if (destination.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("La cuenta destino no esta activa");
        }

        if (loan.getApprovedAmount() <= 0) {
            throw new RuntimeException("El monto aprobado debe ser mayor a cero");
        }

        destination.setBalance(destination.getBalance() + loan.getApprovedAmount());
        loan.setStatus(LoanStatus.DISBURSED);
        loan.setDisbursementDate(LocalDateTime.now().toString());

        accountPort.updateAccount(destination);
        loanPort.saveLoan(loan);

        AuditLog log = new AuditLog();
        log.setAuditLogId("AUDIT-" + System.currentTimeMillis());
        log.setOperationType("DESEMBOLSO_PRESTAMO");
        log.setOperationDateTime(LocalDateTime.now().toString());
        log.setExecutedBy(user);
        log.setUserRole(user.getRole().name());
        log.setAffectedProductId(String.valueOf(loanId));
        log.setDetailData(
            "montoDesembolsado=" + loan.getApprovedAmount() +
            ", cuentaDestino=" + destination.getAccountNumber() +
            ", nuevoSaldo=" + destination.getBalance()
        );
        auditPort.saveLog(log);
    }

    // ─── 5. CONSULTAR PRESTAMOS POR CLIENTE ───────────────────────────────────
    public List<Loan> getLoansByCustomer(User user, String customerId) {
        if (user.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            user.getRole() == UserRole.BUSINESS_CUSTOMER) {
            if (!user.getRelatedId().equals(customerId)) {
                throw new RuntimeException("Acceso denegado: solo puede ver sus propios prestamos");
            }
        }
        return loanPort.findByCustomerId(customerId);
    }

    // ─── 6. CONSULTAR PRESTAMO POR ID ─────────────────────────────────────────
    public Loan getLoanById(User user, int loanId) {
        Loan loan = loanPort.findById(loanId);
        if (user.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            user.getRole() == UserRole.BUSINESS_CUSTOMER) {
            if (!loan.getApplicant().getCustomerId().equals(user.getRelatedId())) {
                throw new RuntimeException("Acceso denegado: no puede ver prestamos de otro cliente");
            }
        }
        return loan;
    }
}
