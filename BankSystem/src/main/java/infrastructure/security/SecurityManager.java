package infrastructure.security;

import domain.model.User;
import domain.enums.UserRole;
import domain.enums.UserStatus;

public class SecurityManager {

    private User currentUser;

    public User login(String username, String password, domain.ports.UserPort userPort) {
        User user = userPort.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Contrasena incorrecta");
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("Usuario inactivo o bloqueado");
        }

        this.currentUser = user;
        System.out.println("Sesion iniciada: " + user.getFullName() + " [" + user.getRole() + "]");
        return user;
    }

    public void logout() {
        System.out.println("Sesion cerrada: " + (currentUser != null ? currentUser.getFullName() : "ninguno"));
        this.currentUser = null;
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            throw new RuntimeException("No hay sesion activa. Debe iniciar sesion primero");
        }
        return currentUser;
    }

    public void requireRole(UserRole... allowedRoles) {
        User user = getCurrentUser();
        for (UserRole role : allowedRoles) {
            if (user.getRole() == role) return;
        }
        throw new RuntimeException("Acceso denegado: su rol [" + user.getRole() + "] no tiene permisos para esta operacion");
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
