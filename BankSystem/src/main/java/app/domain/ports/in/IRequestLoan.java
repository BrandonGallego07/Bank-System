package app.domain.ports.in;
import app.domain.exceptions.BusinessException;
import app.domain.models.Loan;
import app.domain.models.User;
public interface IRequestLoan {
    Loan requestLoan(User requester, Loan loan) throws BusinessException;
}
