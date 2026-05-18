package app.domain.ports.out;
import app.domain.models.Transfer;
import java.util.List;
import java.util.Optional;
public interface ITransferRepository {
    Transfer save(Transfer transfer);
    Optional<Transfer> findById(Long id);
    List<Transfer> findByAccountNumber(String accountNumber);
}
