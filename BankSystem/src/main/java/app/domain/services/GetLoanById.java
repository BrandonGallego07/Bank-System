package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Loan;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.out.ILoanRepository;
import org.springframework.stereotype.Service;

@Service
public class GetLoanById {

    private final ILoanRepository loanRepository;

    public GetLoanById(ILoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan getLoan(User requester, Long loanId) throws BusinessException {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new BusinessException("Prestamo no encontrado"));

        if (requester.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            requester.getRole() == UserRole.BUSINESS_CUSTOMER) {
            if (!loan.getApplicantId().equals(requester.getIdentificationId()))
                throw new BusinessException("Acceso denegado: no puede ver prestamos de otro cliente");
        }

        return loan;
    }
}
