package domain.ports;

import domain.model.User;

public interface UserPort {
    User findById(int id);
    User findByUsername(String username);
    User findByIdentificationId(String identificationId);
    void saveUser(User user);
}
