package domain.services;

import domain.model.Account;
import domain.model.AuditLog;
import domain.model.Customer;
import domain.model.User;
import domain.ports.AccountPort;
import domain.ports.AuditPort;
import domain.enums.AccountStatus;
import domain.enums.UserRole;
import domain.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.List;

public class AccountService {

    private final AccountPort accountPort;
    private final AuditPort auditPort;

    public AccountService(AccountPort accountPort, AuditPort auditPort) {
        this.accountPort = accountPort;
        this.auditPort = auditPort;
    }

    // ─── 1. ABRIR CUENTA ──────────────────────────────────────────────────────
    public Account openAccount(User user, Account account) {
        if (user.getRole() != UserRole.TELLER_EMPLOYEE &&
            user.getRole() != UserRole.COMMERCIAL_EMPLOYEE &&
            user.getRole() != UserRole.INTERNAL_ANALYST) {
            throw new RuntimeException("Acceso denegado: no tiene permisos para abrir cuentas");
        }

        Customer holder = account.getHolder();
        if (holder == null) {
            throw new RuntimeException("La cuenta debe tener un titular");
        }

        // Validar que el cliente este activo
        User holderUser = accountPort.findUserByCustomerId(holder.getCustomerId());
        if (holderUser.getStatus() == UserStatus.INACTIVE || holderUser.getStatus() == UserStatus.BLOCKED) {
            throw new RuntimeException("No se puede abrir cuenta a un cliente inactivo o bloqueado");
        }

        if (account.getAccountNumber() == null || account.getAccountNumber().isEmpty()) {
            throw new RuntimeException("El numero de cuenta es obligatorio");
        }

        account.setStatus(AccountStatus.ACTIVE);
        account.setOpeningDate(LocalDateTime.now().toString());
        accountPort.saveAccount(account);

        AuditLog log = new AuditLog();
        log.setAuditLogId("AUDIT-" + System.currentTimeMillis());
        log.setOperationType("APERTURA_CUENTA");
        log.setOperationDateTime(LocalDateTime.now().toString());
        log.setExecutedBy(user);
        log.setUserRole(user.getRole().name());
        log.setAffectedProductId(account.getAccountNumber());
        log.setDetailData("tipoCuenta=" + account.getAccountType() + ", titular=" + holder.getCustomerId());
        auditPort.saveLog(log);

        return account;
    }

    // ─── 2. BLOQUEAR CUENTA ───────────────────────────────────────────────────
    public void blockAccount(User user, String accountNumber) {
        if (user.getRole() != UserRole.INTERNAL_ANALYST) {
            throw new RuntimeException("Acceso denegado: solo el Analista Interno puede bloquear cuentas");
        }

        Account account = accountPort.findByNumber(accountNumber);
        account.setStatus(AccountStatus.BLOCKED);
        accountPort.updateAccount(account);

        AuditLog log = new AuditLog();
        log.setAuditLogId("AUDIT-" + System.currentTimeMillis());
        log.setOperationType("BLOQUEO_CUENTA");
        log.setOperationDateTime(LocalDateTime.now().toString());
        log.setExecutedBy(user);
        log.setUserRole(user.getRole().name());
        log.setAffectedProductId(accountNumber);
        log.setDetailData("cuenta bloqueada por analista");
        auditPort.saveLog(log);
    }

    // ─── 3. CANCELAR CUENTA ───────────────────────────────────────────────────
    public void cancelAccount(User user, String accountNumber) {
        if (user.getRole() != UserRole.INTERNAL_ANALYST) {
            throw new RuntimeException("Acceso denegado: solo el Analista Interno puede cancelar cuentas");
        }

        Account account = accountPort.findByNumber(accountNumber);
        account.setStatus(AccountStatus.CANCELED);
        accountPort.updateAccount(account);

        AuditLog log = new AuditLog();
        log.setAuditLogId("AUDIT-" + System.currentTimeMillis());
        log.setOperationType("CANCELACION_CUENTA");
        log.setOperationDateTime(LocalDateTime.now().toString());
        log.setExecutedBy(user);
        log.setUserRole(user.getRole().name());
        log.setAffectedProductId(accountNumber);
        log.setDetailData("cuenta cancelada por analista");
        auditPort.saveLog(log);
    }

    // ─── 4. CONSULTAR CUENTA POR NUMERO ───────────────────────────────────────
    public Account getAccountByNumber(User user, String accountNumber) {
        Account account = accountPort.findByNumber(accountNumber);

        // Clientes solo ven sus propias cuentas
        if (user.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            user.getRole() == UserRole.BUSINESS_CUSTOMER ||
            user.getRole() == UserRole.BUSINESS_EMPLOYEE) {
            if (!account.getHolder().getCustomerId().equals(user.getRelatedId())) {
                throw new RuntimeException("Acceso denegado: no puede ver cuentas de otros clientes");
            }
        }
        return account;
    }

    // ─── 5. CONSULTAR CUENTAS POR CLIENTE ─────────────────────────────────────
    public List<Account> getAccountsByCustomer(User user, String customerId) {
        if (user.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            user.getRole() == UserRole.BUSINESS_CUSTOMER) {
            if (!user.getRelatedId().equals(customerId)) {
                throw new RuntimeException("Acceso denegado: solo puede ver sus propias cuentas");
            }
        }
        return accountPort.findByCustomerId(customerId);
    }

    // ─── 6. DEPOSITO (ventanilla) ──────────────────────────────────────────────
    public void deposit(User user, String accountNumber, double amount) {
        if (user.getRole() != UserRole.TELLER_EMPLOYEE) {
            throw new RuntimeException("Acceso denegado: solo el Empleado de Ventanilla puede registrar depositos");
        }

        if (amount <= 0) {
            throw new RuntimeException("El monto del deposito debe ser mayor a cero");
        }

        Account account = accountPort.findByNumber(accountNumber);
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("No se puede depositar en una cuenta inactiva");
        }

        account.setBalance(account.getBalance() + amount);
        accountPort.updateAccount(account);

        AuditLog log = new AuditLog();
        log.setAuditLogId("AUDIT-" + System.currentTimeMillis());
        log.setOperationType("DEPOSITO");
        log.setOperationDateTime(LocalDateTime.now().toString());
        log.setExecutedBy(user);
        log.setUserRole(user.getRole().name());
        log.setAffectedProductId(accountNumber);
        log.setDetailData("monto=" + amount + ", nuevoSaldo=" + account.getBalance());
        auditPort.saveLog(log);
    }

    // ─── 7. RETIRO (ventanilla) ────────────────────────────────────────────────
    public void withdraw(User user, String accountNumber, double amount) {
        if (user.getRole() != UserRole.TELLER_EMPLOYEE) {
            throw new RuntimeException("Acceso denegado: solo el Empleado de Ventanilla puede registrar retiros");
        }

        if (amount <= 0) {
            throw new RuntimeException("El monto del retiro debe ser mayor a cero");
        }

        Account account = accountPort.findByNumber(accountNumber);
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("No se puede retirar de una cuenta inactiva");
        }

        if (account.getBalance() < amount) {
            throw new RuntimeException("Saldo insuficiente para el retiro");
        }

        account.setBalance(account.getBalance() - amount);
        accountPort.updateAccount(account);

        AuditLog log = new AuditLog();
        log.setAuditLogId("AUDIT-" + System.currentTimeMillis());
        log.setOperationType("RETIRO");
        log.setOperationDateTime(LocalDateTime.now().toString());
        log.setExecutedBy(user);
        log.setUserRole(user.getRole().name());
        log.setAffectedProductId(accountNumber);
        log.setDetailData("monto=" + amount + ", nuevoSaldo=" + account.getBalance());
        auditPort.saveLog(log);
    }
}
