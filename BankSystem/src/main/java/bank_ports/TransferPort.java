package bank_ports;

import bank_model.Transfer;

public interface TransferPort {
    Transfer findById(Long id);
    void saveTransfer(Transfer transfer);
}