package app.domain.ports.in;
import app.domain.exceptions.BusinessException;
import app.domain.models.User;
public interface IDisburseLoan {
    void disburseLoan(User requester, Long loanId) throws BusinessException;
}
