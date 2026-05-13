package main;

import application.adapters.*;
import domain.enums.*;
import domain.model.*;
import domain.services.*;
import infrastructure.security.SecurityManager;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== SISTEMA DE GESTION BANCARIA ===");
        System.out.println();

        // -- Adaptadores en memoria --
        InMemoryUserAdapter userAdapter         = new InMemoryUserAdapter();
        InMemoryAccountAdapter accountAdapter   = new InMemoryAccountAdapter(userAdapter);
        InMemoryLoanAdapter loanAdapter         = new InMemoryLoanAdapter();
        InMemoryTransferAdapter transferAdapter = new InMemoryTransferAdapter();
        InMemoryAuditAdapter auditAdapter       = new InMemoryAuditAdapter();

        // -- Servicios --
        UserService userService         = new UserService(userAdapter);
        AccountService accountService   = new AccountService(accountAdapter, auditAdapter);
        LoanService loanService         = new LoanService(loanAdapter, accountAdapter, auditAdapter);
        TransferService transferService = new TransferService(accountAdapter, transferAdapter, auditAdapter);
        AuditService auditService       = new AuditService(auditAdapter);
        SecurityManager security        = new SecurityManager();

        // -- Datos de prueba --

        // Analista interno
        User analista = new User(1, "CLI-001", "Carlos Ramirez", "1000001",
                "carlos@banco.com", "3001234567", "15/06/1985",
                "Calle 10 # 20-30", UserRole.INTERNAL_ANALYST, UserStatus.ACTIVE,
                "carlos01", "pass123");
        userAdapter.saveUser(analista);

        // Cliente persona natural
        User clienteUser = new User(2, "CLI-002", "Laura Gomez", "1000002",
                "laura@email.com", "3109876543", "20/03/1995",
                "Carrera 5 # 15-40", UserRole.NATURAL_PERSON_CUSTOMER, UserStatus.ACTIVE,
                "laura01", "pass456");
        userAdapter.saveUser(clienteUser);

        // Cajero
        User cajero = new User(3, "EMP-001", "Pedro Suarez", "1000003",
                "pedro@banco.com", "3201112233", "10/01/1990",
                "Av 30 # 5-10", UserRole.TELLER_EMPLOYEE, UserStatus.ACTIVE,
                "pedro01", "pass789");
        userAdapter.saveUser(cajero);

        // Empleado de empresa
        User empEmpresa = new User(4, "EMP-002", "Juan Perez", "1000004",
                "juan@empresa.com", "3151112233", "05/07/1988",
                "Calle 80 # 10-20", UserRole.BUSINESS_EMPLOYEE, UserStatus.ACTIVE,
                "juan01", "pass000");
        userAdapter.saveUser(empEmpresa);

        // Supervisor de empresa
        User supervisor = new User(5, "EMP-003", "Ana Torres", "1000005",
                "ana@empresa.com", "3161112233", "12/09/1982",
                "Calle 90 # 5-15", UserRole.BUSINESS_SUPERVISOR, UserStatus.ACTIVE,
                "ana01", "passSuper");
        userAdapter.saveUser(supervisor);

        // Cuentas bancarias
        NaturalPersonCustomer cliente = new NaturalPersonCustomer();
        cliente.setCustomerId("CLI-002");
        cliente.setEmail("laura@email.com");
        cliente.setPhone("3109876543");
        cliente.setAddress("Carrera 5 # 15-40");

        Account cuentaOrigen  = new Account("ACC-001", AccountType.SAVINGS, cliente,
                5000000.0, CurrencyType.COP, AccountStatus.ACTIVE, "01/01/2024");
        Account cuentaDestino = new Account("ACC-002", AccountType.SAVINGS, cliente,
                1000000.0, CurrencyType.COP, AccountStatus.ACTIVE, "01/01/2024");
        Account cuentaEmpresa = new Account("ACC-003", AccountType.BUSINESS, cliente,
                50000000.0, CurrencyType.COP, AccountStatus.ACTIVE, "01/01/2024");

        accountAdapter.saveAccount(cuentaOrigen);
        accountAdapter.saveAccount(cuentaDestino);
        accountAdapter.saveAccount(cuentaEmpresa);

        // ===================================================
        // PRUEBA 1: Login correcto e incorrecto
        // ===================================================
        System.out.println("--- PRUEBA 1: Login y seguridad ---");
        try {
            security.login("carlos01", "pass123", userAdapter);
            System.out.println("Login exitoso: " + security.getCurrentUser().getFullName());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            security.login("laura01", "claveMAL", userAdapter);
        } catch (Exception e) {
            System.out.println("Login fallido (esperado): " + e.getMessage());
        }

        // ===================================================
        // PRUEBA 2: Deposito por cajero
        // ===================================================
        System.out.println();
        System.out.println("--- PRUEBA 2: Deposito por cajero ---");
        try {
            security.logout();
            security.login("pedro01", "pass789", userAdapter);
            User cajeroActivo = security.getCurrentUser();

            double saldoAntes = accountAdapter.findByNumber("ACC-001").getBalance();
            accountService.deposit(cajeroActivo, "ACC-001", 500000.0);
            double saldoDespues = accountAdapter.findByNumber("ACC-001").getBalance();

            System.out.println("Deposito realizado correctamente");
            System.out.println("Saldo antes:  " + saldoAntes);
            System.out.println("Saldo despues: " + saldoDespues);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // ===================================================
        // PRUEBA 3: Solicitud, aprobacion y desembolso de prestamo
        // ===================================================
        System.out.println();
        System.out.println("--- PRUEBA 3: Flujo completo de prestamo ---");
        try {
            security.logout();
            security.login("laura01", "pass456", userAdapter);
            User lauraActiva = security.getCurrentUser();

            Loan prestamo = new Loan();
            prestamo.setLoanId(1);
            prestamo.setLoanType(LoanType.PERSONAL);
            prestamo.setApplicant(cliente);
            prestamo.setRequestedAmount(10000000.0);
            prestamo.setInterestRate(12.5);
            prestamo.setTermMonths(24);
            prestamo.setDestinationAccount(cuentaOrigen);

            loanService.requestLoan(lauraActiva, prestamo);
            System.out.println("Prestamo solicitado. Estado: " + prestamo.getStatus());

            security.logout();
            security.login("carlos01", "pass123", userAdapter);
            User analistaActivo = security.getCurrentUser();

            loanService.approveLoan(analistaActivo, 1, 9000000.0);
            System.out.println("Prestamo aprobado. Estado: " + loanAdapter.findById(1).getStatus());

            loanService.disburseLoan(analistaActivo, 1);
            System.out.println("Prestamo desembolsado. Saldo cuenta destino: "
                    + accountAdapter.findByNumber("ACC-001").getBalance());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // ===================================================
        // PRUEBA 4: Transferencia directa
        // ===================================================
        System.out.println();
        System.out.println("--- PRUEBA 4: Transferencia directa ---");
        try {
            security.logout();
            security.login("laura01", "pass456", userAdapter);
            User lauraActiva = security.getCurrentUser();

            Transfer transfer = new Transfer();
            transfer.setTransferId(1);
            transfer.setSourceAccount(accountAdapter.findByNumber("ACC-001"));
            transfer.setTargetAccount(accountAdapter.findByNumber("ACC-002"));
            transfer.setAmount(200000.0);

            transferService.createTransfer(lauraActiva, transfer);
            System.out.println("Transferencia ejecutada. Estado: " + transfer.getStatus());
            System.out.println("Saldo cuenta origen:  " + accountAdapter.findByNumber("ACC-001").getBalance());
            System.out.println("Saldo cuenta destino: " + accountAdapter.findByNumber("ACC-002").getBalance());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // ===================================================
        // PRUEBA 5: Transferencia empresarial de alto monto
        // ===================================================
        System.out.println();
        System.out.println("--- PRUEBA 5: Transferencia empresarial alto monto ---");
        try {
            security.logout();
            security.login("juan01", "pass000", userAdapter);
            User juanActivo = security.getCurrentUser();

            Transfer transferAlta = new Transfer();
            transferAlta.setTransferId(2);
            transferAlta.setSourceAccount(accountAdapter.findByNumber("ACC-003"));
            transferAlta.setTargetAccount(accountAdapter.findByNumber("ACC-002"));
            transferAlta.setAmount(15000000.0);

            transferService.createTransfer(juanActivo, transferAlta);
            System.out.println("Transferencia creada. Estado: " + transferAlta.getStatus());

            security.logout();
            security.login("ana01", "passSuper", userAdapter);
            User anaActiva = security.getCurrentUser();

            transferService.approveTransfer(anaActiva, 2);
            System.out.println("Transferencia aprobada por supervisor. Estado: "
                    + transferAdapter.findById(2).getStatus());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // ===================================================
        // PRUEBA 6: Consulta de bitacora completa
        // ===================================================
        System.out.println();
        System.out.println("--- PRUEBA 6: Bitacora de operaciones ---");
        try {
            security.logout();
            security.login("carlos01", "pass123", userAdapter);
            User analistaActivo = security.getCurrentUser();

            var logs = auditService.getFullAuditLog(analistaActivo);
            System.out.println("Total de registros en bitacora: " + logs.size());
            for (AuditLog log : logs) {
                System.out.println("  - [" + log.getOperationType() + "] "
                        + "Usuario: " + log.getExecutedBy().getFullName()
                        + " | Fecha: " + log.getOperationDateTime());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== FIN DEL SISTEMA ===");
    }
}
