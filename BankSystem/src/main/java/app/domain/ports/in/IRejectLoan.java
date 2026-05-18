package app.domain.ports.in;
import app.domain.exceptions.BusinessException;
import app.domain.models.User;
public interface IRejectLoan {
    void rejectLoan(User requester, Long loanId) throws BusinessException;
}
