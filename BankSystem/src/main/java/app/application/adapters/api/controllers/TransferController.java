package app.application.adapters.api.controllers;

import app.application.adapters.api.request.TransferRequest;
import app.application.adapters.persistence.sql.repositories.UserJpaRepository;
import app.application.adapters.persistence.sql.entities.UserEntity;
import app.domain.exceptions.BusinessException;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.services.ApproveTransfer;
import app.domain.services.CreateTransfer;
import app.domain.services.RejectTransfer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final CreateTransfer createTransfer;
    private final ApproveTransfer approveTransfer;
    private final RejectTransfer rejectTransfer;
    private final UserJpaRepository userRepository;

    public TransferController(CreateTransfer createTransfer, ApproveTransfer approveTransfer,
                              RejectTransfer rejectTransfer, UserJpaRepository userRepository) {
        this.createTransfer = createTransfer;
        this.approveTransfer = approveTransfer;
        this.rejectTransfer = rejectTransfer;
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

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TransferRequest request, Authentication auth) {
        try {
            User user = getUser(auth);
            Transfer transfer = new Transfer();
            transfer.setSourceAccountNumber(request.getSourceAccountNumber());
            transfer.setTargetAccountNumber(request.getTargetAccountNumber());
            transfer.setAmount(request.getAmount());
            Transfer result = createTransfer.createTransfer(user, transfer);
            return ResponseEntity.ok(result);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approve(@PathVariable Long id, Authentication auth) {
        try {
            approveTransfer.approveTransfer(getUser(auth), id);
            return ResponseEntity.ok("Transferencia aprobada correctamente");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id, Authentication auth) {
        try {
            rejectTransfer.rejectTransfer(getUser(auth), id);
            return ResponseEntity.ok("Transferencia rechazada");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
