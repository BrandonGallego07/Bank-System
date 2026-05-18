package app.application.adapters.persistence.sql.adapters;

import app.application.adapters.persistence.sql.entities.UserEntity;
import app.application.adapters.persistence.sql.repositories.UserJpaRepository;
import app.domain.models.User;
import app.domain.ports.out.IUserRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UserSqlAdapter implements IUserRepository {

    private final UserJpaRepository jpaRepository;

    public UserSqlAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private User toModel(UserEntity e) {
        User u = new User();
        u.setId(e.getId());
        u.setRelatedId(e.getRelatedId());
        u.setFullName(e.getFullName());
        u.setIdentificationId(e.getIdentificationId());
        u.setEmail(e.getEmail());
        u.setPhone(e.getPhone());
        u.setBirthDate(e.getBirthDate());
        u.setAddress(e.getAddress());
        u.setRole(e.getRole());
        u.setStatus(e.getStatus());
        u.setUsername(e.getUsername());
        u.setPassword(e.getPassword());
        return u;
    }

    private UserEntity toEntity(User u) {
        UserEntity e = new UserEntity();
        e.setId(u.getId());
        e.setRelatedId(u.getRelatedId());
        e.setFullName(u.getFullName());
        e.setIdentificationId(u.getIdentificationId());
        e.setEmail(u.getEmail());
        e.setPhone(u.getPhone());
        e.setBirthDate(u.getBirthDate());
        e.setAddress(u.getAddress());
        e.setRole(u.getRole());
        e.setStatus(u.getStatus());
        e.setUsername(u.getUsername());
        e.setPassword(u.getPassword());
        return e;
    }

    @Override
    public User save(User user) {
        return toModel(jpaRepository.save(toEntity(user)));
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id).map(this::toModel);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(this::toModel);
    }

    @Override
    public Optional<User> findByIdentificationId(String identificationId) {
        return jpaRepository.findByIdentificationId(identificationId).map(this::toModel);
    }

    @Override
    public boolean existsByIdentificationId(String identificationId) {
        return jpaRepository.existsByIdentificationId(identificationId);
    }
}
