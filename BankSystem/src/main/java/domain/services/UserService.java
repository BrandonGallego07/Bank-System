package domain.services;

import domain.model.User;
import domain.ports.UserPort;
import domain.enums.UserStatus;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class UserService {

    private final UserPort userPort;

    public UserService(UserPort userPort) {
        this.userPort = userPort;
    }

    // ─── 1. REGISTRAR USUARIO ─────────────────────────────────────────────────
    public User registerUser(User user) {
        validateUser(user);

        if (userPort.findByIdentificationId(user.getIdentificationId()) != null) {
            throw new RuntimeException("Ya existe un usuario con ese numero de identificacion");
        }
        if (userPort.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Ya existe un usuario con ese nombre de usuario");
        }

        user.setStatus(UserStatus.ACTIVE);
        userPort.saveUser(user);
        return user;
    }

    // ─── 2. LOGIN ─────────────────────────────────────────────────────────────
    public User loginUser(String username, String password) {
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

        return user;
    }

    // ─── 3. BLOQUEAR USUARIO ──────────────────────────────────────────────────
    public void blockUser(User requester, int userId) {
        User target = userPort.findById(userId);
        target.setStatus(UserStatus.BLOCKED);
        userPort.saveUser(target);
    }

    // ─── 4. ACTIVAR USUARIO ───────────────────────────────────────────────────
    public void activateUser(User requester, int userId) {
        User target = userPort.findById(userId);
        target.setStatus(UserStatus.ACTIVE);
        userPort.saveUser(target);
    }

    // ─── 5. CONSULTAR USUARIO POR ID ──────────────────────────────────────────
    public User getUserById(User requester, int userId) {
        return userPort.findById(userId);
    }

    // ─── VALIDACIONES INTERNAS ─────────────────────────────────────────────────
    private void validateUser(User user) {
        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            throw new RuntimeException("El nombre completo es obligatorio");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            throw new RuntimeException("El correo electronico no es valido");
        }
        if (user.getPhone() == null || user.getPhone().length() < 7 || user.getPhone().length() > 15) {
            throw new RuntimeException("El telefono debe tener entre 7 y 15 digitos");
        }
        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            throw new RuntimeException("La direccion es obligatoria");
        }
        if (user.getBirthDate() != null) {
            validateAge(user.getBirthDate());
        }
    }

    private void validateAge(String birthDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birth = LocalDate.parse(birthDate, formatter);
        int age = Period.between(birth, LocalDate.now()).getYears();
        if (age < 18) {
            throw new RuntimeException("El usuario debe ser mayor de edad (al menos 18 anos)");
        }
    }
}
