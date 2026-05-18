package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Loan;
import app.domain.models.User;
import app.domain.enums.LoanStatus;
import app.domain.enums.UserRole;
import app.domain.ports.out.ILoanRepository;
import org.springframework.stereotype.Service;

@Service
public class RequestLoan {

    private final ILoanRepository loanRepository;

    public RequestLoan(ILoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan requestLoan(User requester, Loan loan) throws BusinessException {
        if (requester.getRole() != UserRole.NATURAL_PERSON_CUSTOMER &&
            requester.getRole() != UserRole.BUSINESS_CUSTOMER &&
            requester.getRole() != UserRole.COMMERCIAL_EMPLOYEE)
            throw new BusinessException("Acceso denegado: no tiene permisos para solicitar prestamos");

        if (loan.getRequestedAmount() <= 0)
            throw new BusinessException("El monto solicitado debe ser mayor a cero");

        loan.setStatus(LoanStatus.UNDER_REVIEW);
        loan.setApplicantId(requester.getIdentificationId());
        return loanRepository.save(loan);
    }
}
