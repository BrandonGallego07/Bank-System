package app.application.adapters.api.controllers;

import app.application.adapters.api.request.TransferRequest;
import app.application.adapters.persistence.sql.repositories.UserJpaRepository;
import app.application.adapters.persistence.sql.repositories.TransferJpaRepository;
import app.application.adapters.persistence.sql.repositories.AccountJpaRepository;
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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final CreateTransfer createTransfer;
    private final ApproveTransfer approveTransfer;
    private final RejectTransfer rejectTransfer;
    private final UserJpaRepository userRepository;
    private final TransferJpaRepository transferRepository;
    private final AccountJpaRepository accountRepository;

    public TransferController(CreateTransfer createTransfer, ApproveTransfer approveTransfer,
                              RejectTransfer rejectTransfer, UserJpaRepository userRepository,
                              TransferJpaRepository transferRepository,
                              AccountJpaRepository accountRepository) {
        this.createTransfer = createTransfer;
        this.approveTransfer = approveTransfer;
        this.rejectTransfer = rejectTransfer;
        this.userRepository = userRepository;
        this.transferRepository = transferRepository;
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

    // Crear transferencia
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TransferRequest request, Authentication auth) {
        try {
            User user = getUser(auth);
            Transfer transfer = new Transfer();
            transfer.setSourceAccountNumber(request.getSourceAccountNumber());
            transfer.setTargetAccountNumber(request.getTargetAccountNumber());
            transfer.setAmount(request.getAmount());

            Transfer result = createTransfer.createTransfer(user, transfer);

            var cuentaOrigen = accountRepository.findByAccountNumber(request.getSourceAccountNumber()).orElse(null);
            var cuentaDestino = accountRepository.findByAccountNumber(request.getTargetAccountNumber()).orElse(null);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", result.getStatus().name().equals("EXECUTED")
                    ? "Transferencia ejecutada correctamente"
                    : "Transferencia en espera de aprobacion del supervisor");
            response.put("transfereciaId", result.getId());
            response.put("creadaPor", user.getFullName());
            response.put("cuentaOrigen", request.getSourceAccountNumber());
            response.put("cuentaDestino", request.getTargetAccountNumber());
            response.put("monto", request.getAmount());
            response.put("estado", result.getStatus());
            response.put("fechaCreacion", result.getCreationDateTime());

            if (result.getStatus().name().equals("EXECUTED")) {
                response.put("saldoActualOrigen", cuentaOrigen != null ? cuentaOrigen.getBalance() : "No disponible");
                response.put("saldoActualDestino", cuentaDestino != null ? cuentaDestino.getBalance() : "No disponible");
            } else {
                response.put("nota", "Supera el umbral de 10 millones. Debe ser aprobada por el Supervisor de Empresa");
            }

            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Aprobar transferencia
    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approve(@PathVariable Long id, Authentication auth) {
        try {
            User supervisor = getUser(auth);
            approveTransfer.approveTransfer(supervisor, id);

            var transfer = transferRepository.findById(id).orElseThrow();
            var cuentaOrigen = accountRepository.findByAccountNumber(transfer.getSourceAccountNumber()).orElse(null);
            var cuentaDestino = accountRepository.findByAccountNumber(transfer.getTargetAccountNumber()).orElse(null);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Transferencia aprobada y ejecutada correctamente");
            response.put("transferenciaId", id);
            response.put("cuentaOrigen", transfer.getSourceAccountNumber());
            response.put("cuentaDestino", transfer.getTargetAccountNumber());
            response.put("monto", transfer.getAmount());
            response.put("estado", transfer.getStatus());
            response.put("aprobadaPor", supervisor.getFullName());
            response.put("fechaAprobacion", transfer.getApprovalDateTime());
            response.put("saldoActualOrigen", cuentaOrigen != null ? cuentaOrigen.getBalance() : "No disponible");
            response.put("saldoActualDestino", cuentaDestino != null ? cuentaDestino.getBalance() : "No disponible");

            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Rechazar transferencia
    @PutMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id, Authentication auth) {
        try {
            User supervisor = getUser(auth);
            rejectTransfer.rejectTransfer(supervisor, id);

            var transfer = transferRepository.findById(id).orElseThrow();

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Transferencia rechazada");
            response.put("transferenciaId", id);
            response.put("cuentaOrigen", transfer.getSourceAccountNumber());
            response.put("monto", transfer.getAmount());
            response.put("estado", transfer.getStatus());
            response.put("rechazadaPor", supervisor.getFullName());

            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
