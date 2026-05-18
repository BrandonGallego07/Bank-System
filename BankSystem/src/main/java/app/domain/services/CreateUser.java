package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.User;
import app.domain.enums.UserStatus;
import app.domain.ports.out.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUser {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUser(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) throws BusinessException {
        if (user.getFullName() == null || user.getFullName().isEmpty())
            throw new BusinessException("El nombre completo es obligatorio");
        if (user.getEmail() == null || !user.getEmail().contains("@"))
            throw new BusinessException("El correo electronico no es valido");
        if (user.getPhone() == null || user.getPhone().length() < 7 || user.getPhone().length() > 15)
            throw new BusinessException("El telefono debe tener entre 7 y 15 digitos");
        if (userRepository.existsByIdentificationId(user.getIdentificationId()))
            throw new BusinessException("Ya existe un usuario con ese numero de identificacion");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
    }
}
