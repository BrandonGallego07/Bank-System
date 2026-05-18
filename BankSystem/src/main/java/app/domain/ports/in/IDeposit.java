package app.domain.ports.in;
import app.domain.exceptions.BusinessException;
import app.domain.models.User;
public interface IDeposit {
    void deposit(User requester, String accountNumber, double amount) throws BusinessException;
}
