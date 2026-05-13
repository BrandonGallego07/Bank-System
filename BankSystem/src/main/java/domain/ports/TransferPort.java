package domain.ports;

import domain.model.Transfer;
import java.util.List;

public interface TransferPort {
    Transfer findById(int id);
    List<Transfer> findByAccountNumber(String accountNumber);
    void saveTransfer(Transfer transfer);
}
