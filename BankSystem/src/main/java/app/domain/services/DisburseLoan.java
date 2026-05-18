package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Account;
import app.domain.models.Loan;
import app.domain.models.User;
import app.domain.enums.AccountStatus;
import app.domain.enums.LoanStatus;
import app.domain.enums.UserRole;
import app.domain.ports.out.IAccountRepository;
import app.domain.ports.out.ILoanRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class DisburseLoan {

    private final ILoanRepository loanRepository;
    private final IAccountRepository accountRepository;

    public DisburseLoan(ILoanRepository loanRepository, IAccountRepository accountRepository) {
        this.loanRepository = loanRepository;
        this.accountRepository = accountRepository;
    }

    public void disburseLoan(User requester, Long loanId) throws BusinessException {
        if (requester.getRole() != UserRole.INTERNAL_ANALYST)
            throw new BusinessException("Acceso denegado: solo el Analista Interno puede desembolsar prestamos");

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new BusinessException("Prestamo no encontrado"));

        if (loan.getStatus() != LoanStatus.APPROVED)
            throw new BusinessException("El prestamo debe estar aprobado para desembolsar");

        if (loan.getDestinationAccountNumber() == null)
            throw new BusinessException("Debe definir una cuenta destino para el desembolso");

        Account destination = accountRepository.findByAccountNumber(loan.getDestinationAccountNumber())
                .orElseThrow(() -> new BusinessException("Cuenta destino no encontrada"));

        if (destination.getStatus() != AccountStatus.ACTIVE)
            throw new BusinessException("La cuenta destino no esta activa");

        destination.setBalance(destination.getBalance() + loan.getApprovedAmount());
        loan.setStatus(LoanStatus.DISBURSED);
        loan.setDisbursementDate(LocalDateTime.now().toString());

        accountRepository.save(destination);
        loanRepository.save(loan);
    }
}
