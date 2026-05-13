package application.adapters;

import domain.model.Transfer;
import domain.ports.TransferPort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryTransferAdapter implements TransferPort {

    private final List<Transfer> transfers = new ArrayList<>();

    @Override
    public Transfer findById(int id) {
        return transfers.stream()
                .filter(t -> t.getTransferId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Transferencia no encontrada con ID: " + id));
    }

    @Override
    public List<Transfer> findByAccountNumber(String accountNumber) {
        return transfers.stream()
                .filter(t -> t.getSourceAccount().getAccountNumber().equals(accountNumber) ||
                             t.getTargetAccount().getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
    }

    @Override
    public void saveTransfer(Transfer transfer) {
        transfers.removeIf(t -> t.getTransferId() == transfer.getTransferId());
        transfers.add(transfer);
    }
}
