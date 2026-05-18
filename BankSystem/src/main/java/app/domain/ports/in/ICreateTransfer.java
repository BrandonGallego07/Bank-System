package app.domain.ports.in;
import app.domain.exceptions.BusinessException;
import app.domain.models.Transfer;
import app.domain.models.User;
public interface ICreateTransfer {
    Transfer createTransfer(User requester, Transfer transfer) throws BusinessException;
}
