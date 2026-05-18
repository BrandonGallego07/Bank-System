package app.domain.ports.in;
import app.domain.exceptions.BusinessException;
import app.domain.models.User;
public interface IApproveLoan {
    void approveLoan(User requester, Long loanId, double approvedAmount) throws BusinessException;
}
