package app.application.adapters.api.controllers;

import app.application.adapters.api.request.LoanRequest;
import app.application.adapters.persistence.sql.repositories.UserJpaRepository;
import app.application.adapters.persistence.sql.repositories.LoanJpaRepository;
import app.application.adapters.persistence.sql.repositories.AccountJpaRepository;
import app.application.adapters.persistence.sql.entities.UserEntity;
import app.domain.exceptions.BusinessException;
import app.domain.models.Loan;
import app.domain.models.User;
import app.domain.services.ApproveLoan;
import app.domain.services.DisburseLoan;
import app.domain.services.RejectLoan;
import app.domain.services.RequestLoan;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final RequestLoan requestLoan;
    private final ApproveLoan approveLoan;
    private final RejectLoan rejectLoan;
    private final DisburseLoan disburseLoan;
    private final UserJpaRepository userRepository;
    private final LoanJpaRepository loanRepository;
    private final AccountJpaRepository accountRepository;

    public LoanController(RequestLoan requestLoan, ApproveLoan approveLoan,
                          RejectLoan rejectLoan, DisburseLoan disburseLoan,
                          UserJpaRepository userRepository,
                          LoanJpaRepository loanRepository,
                          AccountJpaRepository accountRepository) {
        this.requestLoan = requestLoan;
        this.approveLoan = approveLoan;
        this.rejectLoan = rejectLoan;
        this.disburseLoan = disburseLoan;
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.accountRepository = accountRepository;
    }

    private User getUser(Authentication auth) {
        UserEntity e = userRepository.findByUsername(auth.getName()).orElseThrow();
        User u = new User();
        u.setId(e.getId());
        u.setUsername(e.getUsername());
        u.setRole(e.getRole());
        u.setIdentificationId(e.getIdentificationId());
        u.setFullName(e.getFullName());
        return u;
    }

    // Solicitar prestamo
    @PostMapping("/request")
    public ResponseEntity<?> request(@RequestBody LoanRequest request, Authentication auth) {
        try {
            User user = getUser(auth);
            Loan loan = new Loan();
            loan.setLoanType(request.getLoanType());
            loan.setRequestedAmount(request.getRequestedAmount());
            loan.setInterestRate(request.getInterestRate());
            loan.setTermMonths(request.getTermMonths());
            loan.setDestinationAccountNumber(request.getDestinationAccountNumber());

            Loan result = requestLoan.requestLoan(user, loan);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Solicitud de prestamo registrada correctamente");
            response.put("prestamoId", result.getId());
            response.put("clienteId", result.getApplicantId());
            response.put("solicitadoPor", user.getFullName());
            response.put("tipoPrestamo", result.getLoanType());
            response.put("montoSolicitado", result.getRequestedAmount());
            response.put("tasaInteres", result.getInterestRate() + "%");
            response.put("plazoMeses", result.getTermMonths());
            response.put("cuentaDestino", result.getDestinationAccountNumber());
            response.put("estado", result.getStatus());
            response.put("nota", "En espera de revision por el Analista Interno");

            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Aprobar prestamo
    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approve(@PathVariable Long id,
                                     @RequestParam double approvedAmount,
                                     Authentication auth) {
        try {
            User analista = getUser(auth);
            approveLoan.approveLoan(analista, id, approvedAmount);

            var loan = loanRepository.findById(id).orElseThrow();
            var cliente = userRepository.findByIdentificationId(loan.getApplicantId()).orElse(null);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Prestamo aprobado correctamente");
            response.put("prestamoId", id);
            response.put("clienteId", loan.getApplicantId());
            response.put("nombreCliente", cliente != null ? cliente.getFullName() : "No encontrado");
            response.put("montoSolicitado", loan.getRequestedAmount());
            response.put("montoAprobado", loan.getApprovedAmount());
            response.put("tasaInteres", loan.getInterestRate() + "%");
            response.put("plazoMeses", loan.getTermMonths());
            response.put("estado", loan.getStatus());
            response.put("aprobadoPor", analista.getFullName());
            response.put("fechaAprobacion", loan.getApprovalDate());
            response.put("nota", "Listo para desembolso");

            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Rechazar prestamo
    @PutMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id, Authentication auth) {
        try {
            User analista = getUser(auth);
            rejectLoan.rejectLoan(analista, id);

            var loan = loanRepository.findById(id).orElseThrow();
            var cliente = userRepository.findByIdentificationId(loan.getApplicantId()).orElse(null);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Prestamo rechazado");
            response.put("prestamoId", id);
            response.put("clienteId", loan.getApplicantId());
            response.put("nombreCliente", cliente != null ? cliente.getFullName() : "No encontrado");
            response.put("montoSolicitado", loan.getRequestedAmount());
            response.put("estado", loan.getStatus());
            response.put("rechazadoPor", analista.getFullName());

            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Desembolsar prestamo
    @PutMapping("/disburse/{id}")
    public ResponseEntity<?> disburse(@PathVariable Long id, Authentication auth) {
        try {
            User analista = getUser(auth);
            disburseLoan.disburseLoan(analista, id);

            var loan = loanRepository.findById(id).orElseThrow();
            var cliente = userRepository.findByIdentificationId(loan.getApplicantId()).orElse(null);
            var cuenta = accountRepository.findByAccountNumber(loan.getDestinationAccountNumber()).orElse(null);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Prestamo desembolsado correctamente");
            response.put("prestamoId", id);
            response.put("clienteId", loan.getApplicantId());
            response.put("nombreCliente", cliente != null ? cliente.getFullName() : "No encontrado");
            response.put("montoDesembolsado", loan.getApprovedAmount());
            response.put("cuentaDestino", loan.getDestinationAccountNumber());
            response.put("saldoActualCuenta", cuenta != null ? cuenta.getBalance() : "No disponible");
            response.put("estado", loan.getStatus());
            response.put("desembolsadoPor", analista.getFullName());
            response.put("fechaDesembolso", loan.getDisbursementDate());

            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
