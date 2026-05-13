package domain.services;

import domain.model.AuditLog;
import domain.model.Account;
import domain.model.Transfer;
import domain.model.User;
import domain.ports.AccountPort;
import domain.ports.TransferPort;
import domain.ports.AuditPort;
import domain.enums.TransferStatus;
import domain.enums.AccountStatus;
import domain.enums.UserRole;

import java.time.LocalDateTime;

public class TransferService {

    private static final double HIGH_AMOUNT_THRESHOLD = 10_000_000.0;

    private final AccountPort accountPort;
    private final TransferPort transferPort;
    private final AuditPort auditPort;

    public TransferService(AccountPort accountPort, TransferPort transferPort, AuditPort auditPort) {
        this.accountPort = accountPort;
        this.transferPort = transferPort;
        this.auditPort = auditPort;
    }

    // ─── 1. CREAR TRANSFERENCIA ────────────────────────────────────────────────
    public Transfer createTransfer(User user, Transfer transfer) {
        // Solo estos roles pueden crear transferencias
        if (user.getRole() != UserRole.NATURAL_PERSON_CUSTOMER &&
            user.getRole() != UserRole.BUSINESS_EMPLOYEE &&
            user.getRole() != UserRole.COMMERCIAL_EMPLOYEE) {
            throw new RuntimeException("Acceso denegado: no tiene permisos para crear transferencias");
        }

        if (transfer.getAmount() <= 0) {
            throw new RuntimeException("El monto debe ser mayor a cero");
        }

        Account origin = transfer.getSourceAccount();
        if (origin.getStatus() == AccountStatus.BLOCKED || origin.getStatus() == AccountStatus.CANCELED) {
            throw new RuntimeException("La cuenta origen esta bloqueada o cancelada");
        }

        transfer.setCreatedBy(user);
        transfer.setCreationDateTime(LocalDateTime.now().toString());

        // Si es empleado de empresa y supera el umbral → espera aprobacion
        if (user.getRole() == UserRole.BUSINESS_EMPLOYEE && transfer.getAmount() > HIGH_AMOUNT_THRESHOLD) {
            transfer.setStatus(TransferStatus.WAITING_FOR_APPROVAL);
        } else {
            // Ejecutar directo
            executeTransferFunds(transfer, user);
        }

        transferPort.saveTransfer(transfer);
        return transfer;
    }

    // ─── 2. EJECUTAR TRANSFERENCIA DIRECTA ────────────────────────────────────
    private void executeTransferFunds(Transfer transfer, User user) {
        Account origin = transfer.getSourceAccount();
        Account destination = transfer.getTargetAccount();

        if (origin.getBalance() < transfer.getAmount()) {
            throw new RuntimeException("Saldo insuficiente en la cuenta origen");
        }

        double balanceBeforeOrigin = origin.getBalance();
        double balanceBeforeDestination = destination.getBalance();

        origin.setBalance(origin.getBalance() - transfer.getAmount());
        destination.setBalance(destination.getBalance() + transfer.getAmount());
        transfer.setStatus(TransferStatus.EXECUTED);

        accountPort.updateAccount(origin);
        accountPort.updateAccount(destination);

        // Bitacora
        AuditLog log = new AuditLog();
        log.setAuditLogId("AUDIT-" + System.currentTimeMillis());
        log.setOperationType("TRANSFERENCIA_EJECUTADA");
        log.setOperationDateTime(LocalDateTime.now().toString());
        log.setExecutedBy(user);
        log.setUserRole(user.getRole().name());
        log.setAffectedProductId(String.valueOf(transfer.getTransferId()));
        log.setDetailData(
            "monto=" + transfer.getAmount() +
            ", saldoAntesOrigen=" + balanceBeforeOrigin +
            ", saldoDespuesOrigen=" + origin.getBalance() +
            ", saldoAntesDestino=" + balanceBeforeDestination +
            ", saldoDespuesDestino=" + destination.getBalance()
        );
        auditPort.saveLog(log);
    }

    // ─── 3. APROBAR TRANSFERENCIA (Supervisor de empresa) ─────────────────────
    public void approveTransfer(User user, int transferId) {
        if (user.getRole() != UserRole.BUSINESS_SUPERVISOR) {
            throw new RuntimeException("Acceso denegado: solo el Supervisor de Empresa puede aprobar transferencias");
        }

        Transfer transfer = transferPort.findById(transferId);

        if (transfer.getStatus() != TransferStatus.WAITING_FOR_APPROVAL) {
            throw new RuntimeException("La transferencia no esta en espera de aprobacion");
        }

        // Verificar que no haya vencido
        expireIfOverdue(transfer);
        if (transfer.getStatus() == TransferStatus.EXPIRED) {
            throw new RuntimeException("La transferencia ya vencio por falta de aprobacion a tiempo");
        }

        transfer.setApprovedBy(user);
        transfer.setApprovalDateTime(LocalDateTime.now().toString());

        executeTransferFunds(transfer, user);
        transferPort.saveTransfer(transfer);
    }

    // ─── 4. RECHAZAR TRANSFERENCIA (Supervisor de empresa) ────────────────────
    public void rejectTransfer(User user, int transferId) {
        if (user.getRole() != UserRole.BUSINESS_SUPERVISOR) {
            throw new RuntimeException("Acceso denegado: solo el Supervisor de Empresa puede rechazar transferencias");
        }

        Transfer transfer = transferPort.findById(transferId);

        if (transfer.getStatus() != TransferStatus.WAITING_FOR_APPROVAL) {
            throw new RuntimeException("La transferencia no esta en espera de aprobacion");
        }

        transfer.setStatus(TransferStatus.REJECTED);
        transferPort.saveTransfer(transfer);

        AuditLog log = new AuditLog();
        log.setAuditLogId("AUDIT-" + System.currentTimeMillis());
        log.setOperationType("TRANSFERENCIA_RECHAZADA");
        log.setOperationDateTime(LocalDateTime.now().toString());
        log.setExecutedBy(user);
        log.setUserRole(user.getRole().name());
        log.setAffectedProductId(String.valueOf(transferId));
        log.setDetailData("Transferencia rechazada por supervisor");
        auditPort.saveLog(log);
    }

    // ─── 5. VENCER TRANSFERENCIA (60 minutos sin aprobacion) ──────────────────
    public void expireIfOverdue(Transfer transfer) {
        if (transfer.getStatus() != TransferStatus.WAITING_FOR_APPROVAL) return;

        LocalDateTime created = LocalDateTime.parse(transfer.getCreationDateTime());
        if (LocalDateTime.now().isAfter(created.plusMinutes(60))) {
            transfer.setStatus(TransferStatus.EXPIRED);
            transferPort.saveTransfer(transfer);

            AuditLog log = new AuditLog();
            log.setAuditLogId("AUDIT-" + System.currentTimeMillis());
            log.setOperationType("TRANSFERENCIA_VENCIDA");
            log.setOperationDateTime(LocalDateTime.now().toString());
            log.setExecutedBy(transfer.getCreatedBy());
            log.setUserRole(transfer.getCreatedBy().getRole().name());
            log.setAffectedProductId(String.valueOf(transfer.getTransferId()));
            log.setDetailData("motivo=vencida por falta de aprobacion en el tiempo establecido, fechaVencimiento=" + LocalDateTime.now());
            auditPort.saveLog(log);
        }
    }

    // ─── 6. CONSULTAR TRANSFERENCIAS POR CUENTA ───────────────────────────────
    public java.util.List<Transfer> getTransfersByAccount(User user, String accountNumber) {
        Account account = accountPort.findByNumber(accountNumber);
        // El usuario solo puede ver sus propias transferencias, salvo analista
        if (user.getRole() != UserRole.INTERNAL_ANALYST &&
            !user.getRelatedId().equals(account.getHolder().getCustomerId())) {
            throw new RuntimeException("Acceso denegado: no puede ver transferencias de otra cuenta");
        }
        return transferPort.findByAccountNumber(accountNumber);
    }
}
