package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.out.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class GetUserById {

    private final IUserRepository userRepository;

    public GetUserById(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(User requester, Long userId) throws BusinessException {
        if (requester.getRole() != UserRole.INTERNAL_ANALYST)
            throw new BusinessException("Acceso denegado: no tiene permisos para consultar usuarios");

        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));
    }
}
