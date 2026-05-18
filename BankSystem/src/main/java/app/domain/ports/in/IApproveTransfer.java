package app.domain.ports.in;
import app.domain.exceptions.BusinessException;
import app.domain.models.User;
public interface IApproveTransfer {
    void approveTransfer(User requester, Long transferId) throws BusinessException;
}
