package app.application.adapters.api.controllers;

import app.application.adapters.api.request.AccountRequest;
import app.application.adapters.api.request.DepositRequest;
import app.application.adapters.persistence.sql.entities.UserEntity;
import app.application.adapters.persistence.sql.repositories.UserJpaRepository;
import app.application.adapters.persistence.sql.repositories.AccountJpaRepository;
import app.domain.exceptions.BusinessException;
import app.domain.models.Account;
import app.domain.models.User;
import app.domain.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final OpenAccount openAccount;
    private final Deposit deposit;
    private final Withdraw withdraw;
    private final BlockAccount blockAccount;
    private final CancelAccount cancelAccount;
    private final GetAccountByNumber getAccountByNumber;
    private final GetAccountsByCustomer getAccountsByCustomer;
    private final UserJpaRepository userRepository;
    private final AccountJpaRepository accountJpaRepository;

    public AccountController(OpenAccount openAccount, Deposit deposit, Withdraw withdraw,
                             BlockAccount blockAccount, CancelAccount cancelAccount,
                             GetAccountByNumber getAccountByNumber,
                             GetAccountsByCustomer getAccountsByCustomer,
                             UserJpaRepository userRepository,
                             AccountJpaRepository accountJpaRepository) {
        this.openAccount = openAccount;
        this.deposit = deposit;
        this.withdraw = withdraw;
        this.blockAccount = blockAccount;
        this.cancelAccount = cancelAccount;
        this.getAccountByNumber = getAccountByNumber;
        this.getAccountsByCustomer = getAccountsByCustomer;
        this.userRepository = userRepository;
        this.accountJpaRepository = accountJpaRepository;
    }

    private User getUser(Authentication auth) {
        UserEntity e = userRepository.findByUsername(auth.getName()).orElseThrow();
        User u = new User();
        u.setId(e.getId());
        u.setUsername(e.getUsername());
        u.setRole(e.getRole());
        u.setIdentificationId(e.getIdentificationId());
        return u;
    }

    // Consultar cuenta por numero
    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccount(@PathVariable String accountNumber, Authentication auth) {
        try {
            Account account = getAccountByNumber.getAccount(getUser(auth), accountNumber);
            return ResponseEntity.ok(account);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Consultar cuentas de un cliente
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getAccountsByCustomer(@PathVariable String customerId, Authentication auth) {
        try {
            List<Account> accounts = getAccountsByCustomer.getAccounts(getUser(auth), customerId);
            return ResponseEntity.ok(accounts);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Deposito - devuelve cuenta con saldo actualizado
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request, Authentication auth) {
        try {
            deposit.deposit(getUser(auth), request.getAccountNumber(), request.getAmount());
            // Devolver la cuenta con el saldo actualizado
            Account updated = getAccountByNumber.getAccount(getUser(auth), request.getAccountNumber());
            return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
                put("mensaje", "Deposito realizado correctamente");
                put("numeroCuenta", updated.getAccountNumber());
                put("saldoAnterior", updated.getBalance() - request.getAmount());
                put("montoDepositado", request.getAmount());
                put("saldoActual", updated.getBalance());
            }});
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Retiro - devuelve cuenta con saldo actualizado
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody DepositRequest request, Authentication auth) {
        try {
            withdraw.withdraw(getUser(auth), request.getAccountNumber(), request.getAmount());
            Account updated = getAccountByNumber.getAccount(getUser(auth), request.getAccountNumber());
            return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
                put("mensaje", "Retiro realizado correctamente");
                put("numeroCuenta", updated.getAccountNumber());
                put("saldoAnterior", updated.getBalance() + request.getAmount());
                put("montoRetirado", request.getAmount());
                put("saldoActual", updated.getBalance());
            }});
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Bloquear cuenta
    @PutMapping("/block/{accountNumber}")
    public ResponseEntity<?> blockAccount(@PathVariable String accountNumber, Authentication auth) {
        try {
            blockAccount.blockAccount(getUser(auth), accountNumber);
            return ResponseEntity.ok("Cuenta bloqueada correctamente");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Cancelar cuenta
    @PutMapping("/cancel/{accountNumber}")
    public ResponseEntity<?> cancelAccount(@PathVariable String accountNumber, Authentication auth) {
        try {
            cancelAccount.cancelAccount(getUser(auth), accountNumber);
            return ResponseEntity.ok("Cuenta cancelada correctamente");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
