package app.domain.ports.in;
import app.domain.exceptions.BusinessException;
import app.domain.models.User;
public interface ICreateUser {
    User createUser(User user) throws BusinessException;
}
