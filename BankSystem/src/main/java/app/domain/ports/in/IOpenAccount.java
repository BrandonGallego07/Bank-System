package app.domain.ports.in;
import app.domain.exceptions.BusinessException;
import app.domain.models.Account;
import app.domain.models.User;
public interface IOpenAccount {
    Account openAccount(User requester, Account account) throws BusinessException;
}
