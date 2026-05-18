package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Loan;
import app.domain.models.User;
import app.domain.enums.LoanStatus;
import app.domain.enums.UserRole;
import app.domain.ports.out.ILoanRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ApproveLoan {

    private final ILoanRepository loanRepository;

    public ApproveLoan(ILoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public void approveLoan(User requester, Long loanId, double approvedAmount) throws BusinessException {
        if (requester.getRole() != UserRole.INTERNAL_ANALYST)
            throw new BusinessException("Acceso denegado: solo el Analista Interno puede aprobar prestamos");

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new BusinessException("Prestamo no encontrado"));

        if (loan.getStatus() != LoanStatus.UNDER_REVIEW)
            throw new BusinessException("El prestamo no esta en revision");

        if (approvedAmount <= 0)
            throw new BusinessException("El monto aprobado debe ser mayor a cero");

        loan.setApprovedAmount(approvedAmount);
        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovalDate(LocalDateTime.now().toString());
        loanRepository.save(loan);
    }
}
