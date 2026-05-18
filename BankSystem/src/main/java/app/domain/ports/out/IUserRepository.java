package app.domain.ports.out;
import app.domain.models.User;
import java.util.Optional;
public interface IUserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByIdentificationId(String identificationId);
    boolean existsByIdentificationId(String identificationId);
}
