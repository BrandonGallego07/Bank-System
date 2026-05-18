package app.domain.ports.out;
import app.domain.models.Loan;
import java.util.List;
import java.util.Optional;
public interface ILoanRepository {
    Loan save(Loan loan);
    Optional<Loan> findById(Long id);
    List<Loan> findByApplicantId(String applicantId);
}
