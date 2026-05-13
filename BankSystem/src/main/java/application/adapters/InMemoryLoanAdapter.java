package application.adapters;

import domain.model.Loan;
import domain.ports.LoanPort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryLoanAdapter implements LoanPort {

    private final List<Loan> loans = new ArrayList<>();

    @Override
    public Loan findById(int id) {
        return loans.stream()
                .filter(l -> l.getLoanId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Prestamo no encontrado con ID: " + id));
    }

    @Override
    public List<Loan> findByCustomerId(String customerId) {
        return loans.stream()
                .filter(l -> l.getApplicant().getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public void saveLoan(Loan loan) {
        loans.removeIf(l -> l.getLoanId() == loan.getLoanId());
        loans.add(loan);
    }
}
