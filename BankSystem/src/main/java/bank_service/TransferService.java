package bank_service;

import bank_model.AuditLog;
import bank_model.Account;
import bank_model.Transfer;
import bank_model.User;

import bank_ports.AccountPort;
import bank_ports.TransferPort;
import bank_ports.AuditPort;

import bank_enums.TransferStatus;
import bank_enums.AccountStatus;

import java.time.LocalDateTime;

public class TransferService {

    private AccountPort accountPort;
    private TransferPort transferPort;
    private AuditPort auditPort;

    public TransferService(AccountPort accountPort,
                           TransferPort transferPort,
                           AuditPort auditPort) {
        this.accountPort = accountPort;
        this.transferPort = transferPort;
        this.auditPort = auditPort;
    }

    public void executeTransfer(User user, Long transferId) {

        // Obtener transferencia
        Transfer transfer = transferPort.findById(transferId);

        Account origin = transfer.getSourceAccount();
        Account destination = transfer.getTargetAccount();

        // 🔐 VALIDACIÓN DE SEGURIDAD
        if (!user.getRelatedId().equals(origin.getHolder().getCustomerId())) {
        throw new RuntimeException("Access denied");
        }

        // Validar estado cuenta origen
        if (origin.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("Account not active");
        }

        // Validar monto
        if (transfer.getAmount() <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        // Validar saldo
        if (origin.getBalance() < transfer.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        // Ejecutar transferencia
        origin.setBalance(origin.getBalance() - transfer.getAmount());
        destination.setBalance(destination.getBalance() + transfer.getAmount());

        // Cambiar estado
        transfer.setStatus(TransferStatus.APPROVED);

        // Persistencia
        accountPort.updateAccount(origin);
        accountPort.updateAccount(destination);
        transferPort.saveTransfer(transfer);

        // 🧾 AUDITORÍA
        AuditLog log = new AuditLog();
        log.setOperationType("TRANSFER");
        log.setExecutedBy(user);
        log.setAffectedProductId(String.valueOf(transfer.getTransferId()));
        log.setDetailData("Transfer approved successfully");
        log.setOperationDateTime(LocalDateTime.now().toString());

        auditPort.saveLog(log);
    }
}