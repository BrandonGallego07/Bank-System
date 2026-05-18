package app.infrastructure;

import app.application.adapters.persistence.sql.repositories.UserJpaRepository;
import app.application.adapters.persistence.sql.repositories.AccountJpaRepository;
import app.domain.enums.*;
import app.domain.exceptions.BusinessException;
import app.domain.models.Account;
import app.domain.models.User;
import app.domain.services.CreateUser;
import app.domain.services.OpenAccount;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationRunner {

    private final UserJpaRepository userRepository;
    private final AccountJpaRepository accountRepository;
    private final CreateUser createUser;
    private final OpenAccount openAccount;

    public DataSeeder(UserJpaRepository userRepository, AccountJpaRepository accountRepository,
                      CreateUser createUser, OpenAccount openAccount) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.createUser = createUser;
        this.openAccount = openAccount;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() > 0) return;

        // Analista interno
        User analista = buildUser("1000001", "Carlos Ramirez", "1000001",
                "carlos@banco.com", "3001234567", "15/06/1985",
                "Calle 10 # 20-30", UserRole.INTERNAL_ANALYST, "analista01", "pass123");
        createUser.createUser(analista);

        // Cajero
        User cajero = buildUser("1000002", "Pedro Suarez", "1000002",
                "pedro@banco.com", "3201112233", "10/01/1990",
                "Av 30 # 5-10", UserRole.TELLER_EMPLOYEE, "cajero01", "pass123");
        createUser.createUser(cajero);

        // Cliente persona natural
        User cliente = buildUser("1000003", "Laura Gomez", "1000003",
                "laura@email.com", "3109876543", "20/03/1995",
                "Carrera 5 # 15-40", UserRole.NATURAL_PERSON_CUSTOMER, "laura01", "pass123");
        createUser.createUser(cliente);

        // Empleado empresa
        User empEmpresa = buildUser("1000004", "Juan Perez", "1000004",
                "juan@empresa.com", "3151112233", "05/07/1988",
                "Calle 80 # 10-20", UserRole.BUSINESS_EMPLOYEE, "juan01", "pass123");
        createUser.createUser(empEmpresa);

        // Supervisor empresa
        User supervisor = buildUser("1000005", "Ana Torres", "1000005",
                "ana@empresa.com", "3161112233", "12/09/1982",
                "Calle 90 # 5-15", UserRole.BUSINESS_SUPERVISOR, "ana01", "pass123");
        createUser.createUser(supervisor);

        // Empleado comercial
        User comercial = buildUser("1000006", "Mario Lopez", "1000006",
                "mario@banco.com", "3171112233", "08/03/1987",
                "Carrera 20 # 10-30", UserRole.COMMERCIAL_EMPLOYEE, "mario01", "pass123");
        createUser.createUser(comercial);

        // Cuentas de prueba - el cajero abre las cuentas
        User cajeroModel = new User();
        cajeroModel.setRole(UserRole.TELLER_EMPLOYEE);

        Account cuenta1 = buildAccount("ACC-001", AccountType.SAVINGS, "1000003", 5000000.0);
        Account cuenta2 = buildAccount("ACC-002", AccountType.SAVINGS, "1000003", 1000000.0);
        Account cuenta3 = buildAccount("ACC-003", AccountType.BUSINESS, "1000004", 50000000.0);

        openAccount.openAccount(cajeroModel, cuenta1);
        openAccount.openAccount(cajeroModel, cuenta2);
        openAccount.openAccount(cajeroModel, cuenta3);

        System.out.println("=== Datos de prueba cargados correctamente ===");
        System.out.println("Usuarios creados: analista01, cajero01, laura01, juan01, ana01, mario01");
        System.out.println("Contrasena de todos: pass123");
        System.out.println("Cuentas: ACC-001, ACC-002, ACC-003");
    }

    private User buildUser(String relatedId, String fullName, String identificationId,
                            String email, String phone, String birthDate,
                            String address, UserRole role, String username, String password) {
        User u = new User();
        u.setRelatedId(relatedId);
        u.setFullName(fullName);
        u.setIdentificationId(identificationId);
        u.setEmail(email);
        u.setPhone(phone);
        u.setBirthDate(birthDate);
        u.setAddress(address);
        u.setRole(role);
        u.setUsername(username);
        u.setPassword(password);
        return u;
    }

    private Account buildAccount(String accountNumber, AccountType type, String holderId, double balance) {
        Account a = new Account();
        a.setAccountNumber(accountNumber);
        a.setAccountType(type);
        a.setHolderId(holderId);
        a.setBalance(balance);
        a.setCurrency(CurrencyType.COP);
        return a;
    }
}
