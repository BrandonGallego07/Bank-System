package app.application.adapters.api.controllers;

import app.application.adapters.api.request.LoginRequest;
import app.application.adapters.api.response.AuthResponse;
import app.domain.exceptions.BusinessException;
import app.domain.models.User;
import app.domain.services.LoginUser;
import app.infrastructure.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginUser loginUser;
    private final JwtUtil jwtUtil;

    public AuthController(LoginUser loginUser, JwtUtil jwtUtil) {
        this.loginUser = loginUser;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = loginUser.login(request.getUsername(), request.getPassword());
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
            return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole().name()));
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
