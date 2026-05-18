package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Loan;
import app.domain.models.User;
import app.domain.enums.LoanStatus;
import app.domain.enums.UserRole;
import app.domain.ports.out.ILoanRepository;
import org.springframework.stereotype.Service;

@Service
public class RejectLoan {

    private final ILoanRepository loanRepository;

    public RejectLoan(ILoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public void rejectLoan(User requester, Long loanId) throws BusinessException {
        if (requester.getRole() != UserRole.INTERNAL_ANALYST)
            throw new BusinessException("Acceso denegado: solo el Analista Interno puede rechazar prestamos");

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new BusinessException("Prestamo no encontrado"));

        if (loan.getStatus() != LoanStatus.UNDER_REVIEW)
            throw new BusinessException("El prestamo no esta en revision");

        loan.setStatus(LoanStatus.REJECTED);
        loanRepository.save(loan);
    }
}
