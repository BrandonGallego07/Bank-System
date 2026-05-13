package domain.ports;

import domain.model.Loan;
import java.util.List;

public interface LoanPort {
    Loan findById(int id);
    List<Loan> findByCustomerId(String customerId);
    void saveLoan(Loan loan);
}
