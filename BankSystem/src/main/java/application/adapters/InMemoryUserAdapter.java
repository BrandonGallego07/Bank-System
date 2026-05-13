package application.adapters;

import domain.model.User;
import domain.ports.UserPort;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserAdapter implements UserPort {

    private final List<User> users = new ArrayList<>();

    @Override
    public User findById(int id) {
        return users.stream()
                .filter(u -> u.getUserId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public User findByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User findByIdentificationId(String identificationId) {
        return users.stream()
                .filter(u -> u.getIdentificationId().equals(identificationId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void saveUser(User user) {
        users.removeIf(u -> u.getUserId() == user.getUserId());
        users.add(user);
    }
}
