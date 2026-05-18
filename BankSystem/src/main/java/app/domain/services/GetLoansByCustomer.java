package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Loan;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.out.ILoanRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GetLoansByCustomer {

    private final ILoanRepository loanRepository;

    public GetLoansByCustomer(ILoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public List<Loan> getLoans(User requester, String customerId) throws BusinessException {
        if (requester.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            requester.getRole() == UserRole.BUSINESS_CUSTOMER) {
            if (!requester.getIdentificationId().equals(customerId))
                throw new BusinessException("Acceso denegado: solo puede ver sus propios prestamos");
        }

        return loanRepository.findByApplicantId(customerId);
    }
}
