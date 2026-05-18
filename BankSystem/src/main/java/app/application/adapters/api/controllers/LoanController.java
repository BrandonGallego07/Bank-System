package app.application.adapters.api.controllers;

import app.application.adapters.api.request.LoanRequest;
import app.application.adapters.persistence.sql.repositories.UserJpaRepository;
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

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final RequestLoan requestLoan;
    private final ApproveLoan approveLoan;
    private final RejectLoan rejectLoan;
    private final DisburseLoan disburseLoan;
    private final UserJpaRepository userRepository;

    public LoanController(RequestLoan requestLoan, ApproveLoan approveLoan,
                          RejectLoan rejectLoan, DisburseLoan disburseLoan,
                          UserJpaRepository userRepository) {
        this.requestLoan = requestLoan;
        this.approveLoan = approveLoan;
        this.rejectLoan = rejectLoan;
        this.disburseLoan = disburseLoan;
        this.userRepository = userRepository;
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
            return ResponseEntity.ok(result);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approve(@PathVariable Long id,
                                     @RequestParam double approvedAmount,
                                     Authentication auth) {
        try {
            approveLoan.approveLoan(getUser(auth), id, approvedAmount);
            return ResponseEntity.ok("Prestamo aprobado correctamente");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id, Authentication auth) {
        try {
            rejectLoan.rejectLoan(getUser(auth), id);
            return ResponseEntity.ok("Prestamo rechazado");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/disburse/{id}")
    public ResponseEntity<?> disburse(@PathVariable Long id, Authentication auth) {
        try {
            disburseLoan.disburseLoan(getUser(auth), id);
            return ResponseEntity.ok("Prestamo desembolsado correctamente");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
