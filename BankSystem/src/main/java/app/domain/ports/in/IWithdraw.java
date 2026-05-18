package app.domain.ports.in;
import app.domain.exceptions.BusinessException;
import app.domain.models.User;
public interface IWithdraw {
    void withdraw(User requester, String accountNumber, double amount) throws BusinessException;
}
