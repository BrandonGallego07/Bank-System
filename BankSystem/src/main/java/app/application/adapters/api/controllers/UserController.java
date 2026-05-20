package app.application.adapters.api.controllers;

import app.application.adapters.api.request.CreateUserRequest;
import app.application.adapters.persistence.sql.entities.UserEntity;
import app.application.adapters.persistence.sql.repositories.UserJpaRepository;
import app.domain.exceptions.BusinessException;
import app.domain.models.User;
import app.domain.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUser createUser;
    private final BlockUser blockUser;
    private final ActivateUser activateUser;
    private final GetUserById getUserById;
    private final UserJpaRepository userRepository;

    public UserController(CreateUser createUser, BlockUser blockUser,
                          ActivateUser activateUser, GetUserById getUserById,
                          UserJpaRepository userRepository) {
        this.createUser = createUser;
        this.blockUser = blockUser;
        this.activateUser = activateUser;
        this.getUserById = getUserById;
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
    public ResponseEntity<?> create(@RequestBody CreateUserRequest request, Authentication auth) {
        try {
            User user = new User();
            user.setFullName(request.getFullName());
            user.setIdentificationId(request.getIdentificationId());
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setBirthDate(request.getBirthDate());
            user.setAddress(request.getAddress());
            user.setRole(request.getRole());
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            User created = createUser.createUser(user);
            return ResponseEntity.ok(created);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id, Authentication auth) {
        try {
            User user = getUserById.getUser(getUser(auth), id);
            return ResponseEntity.ok(user);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<?> block(@PathVariable Long id, Authentication auth) {
        try {
            blockUser.blockUser(getUser(auth), id);
            return ResponseEntity.ok("Usuario bloqueado correctamente");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activate(@PathVariable Long id, Authentication auth) {
        try {
            activateUser.activateUser(getUser(auth), id);
            return ResponseEntity.ok("Usuario activado correctamente");
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
