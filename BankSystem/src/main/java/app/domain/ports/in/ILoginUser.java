package app.domain.ports.in;
import app.domain.exceptions.BusinessException;
import app.domain.models.User;
public interface ILoginUser {
    User login(String username, String password) throws BusinessException;
}
