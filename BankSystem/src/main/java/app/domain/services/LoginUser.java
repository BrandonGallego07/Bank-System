package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.User;
import app.domain.enums.UserStatus;
import app.domain.ports.out.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUser {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginUser(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(String username, String password) throws BusinessException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BusinessException("Contrasena incorrecta");

        if (user.getStatus() != UserStatus.ACTIVE)
            throw new BusinessException("Usuario inactivo o bloqueado");

        return user;
    }
}
