package app.domain.ports.in;
import app.domain.exceptions.BusinessException;
import app.domain.models.User;
public interface IRejectTransfer {
    void rejectTransfer(User requester, Long transferId) throws BusinessException;
}
