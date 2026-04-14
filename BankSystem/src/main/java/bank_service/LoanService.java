package bank_service;

import bank_model.Loan;
import bank_model.User;

import bank_ports.LoanPort;

import bank_enums.LoanStatus;
import bank_enums.UserRole;

public class LoanService {

    private LoanPort loanPort;

    public LoanService(LoanPort loanPort) {
        this.loanPort = loanPort;
    }

    public void approveLoan(User user, int loanId, double approvedAmount) {

        Loan loan = loanPort.findById(loanId);

        // Validar rol
        if (user.getRole() != UserRole.INTERNAL_ANALYST) {
            throw new RuntimeException("Access denied");
        }

        // Validar estado
        if (loan.getStatus() != LoanStatus.UNDER_REVIEW) {
            throw new RuntimeException("Loan not in review");
        }

        // Validar monto
        if (approvedAmount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        // Aprobar
        loan.setApprovedAmount(approvedAmount);
        loan.setStatus(LoanStatus.APPROVED);

        loanPort.saveLoan(loan);
    }
}