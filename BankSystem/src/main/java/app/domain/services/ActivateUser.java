package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.enums.UserStatus;
import app.domain.ports.out.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivateUser {

    private final IUserRepository userRepository;

    public ActivateUser(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void activateUser(User requester, Long userId) throws BusinessException {
        if (requester.getRole() != UserRole.INTERNAL_ANALYST)
            throw new BusinessException("Acceso denegado: solo el Analista Interno puede activar usuarios");

        User target = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        target.setStatus(UserStatus.ACTIVE);
        userRepository.save(target);
    }
}
