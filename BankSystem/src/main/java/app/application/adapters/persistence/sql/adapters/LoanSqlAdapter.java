package app.application.adapters.persistence.sql.adapters;

import app.application.adapters.persistence.sql.entities.LoanEntity;
import app.application.adapters.persistence.sql.repositories.LoanJpaRepository;
import app.domain.models.Loan;
import app.domain.ports.out.ILoanRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LoanSqlAdapter implements ILoanRepository {

    private final LoanJpaRepository jpaRepository;

    public LoanSqlAdapter(LoanJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private Loan toModel(LoanEntity e) {
        Loan l = new Loan();
        l.setId(e.getId());
        l.setLoanType(e.getLoanType());
        l.setApplicantId(e.getApplicantId());
        l.setRequestedAmount(e.getRequestedAmount());
        l.setApprovedAmount(e.getApprovedAmount());
        l.setInterestRate(e.getInterestRate());
        l.setTermMonths(e.getTermMonths());
        l.setStatus(e.getStatus());
        l.setApprovalDate(e.getApprovalDate());
        l.setDisbursementDate(e.getDisbursementDate());
        l.setDestinationAccountNumber(e.getDestinationAccountNumber());
        return l;
    }

    private LoanEntity toEntity(Loan l) {
        LoanEntity e = new LoanEntity();
        e.setId(l.getId());
        e.setLoanType(l.getLoanType());
        e.setApplicantId(l.getApplicantId());
        e.setRequestedAmount(l.getRequestedAmount());
        e.setApprovedAmount(l.getApprovedAmount());
        e.setInterestRate(l.getInterestRate());
        e.setTermMonths(l.getTermMonths());
        e.setStatus(l.getStatus());
        e.setApprovalDate(l.getApprovalDate());
        e.setDisbursementDate(l.getDisbursementDate());
        e.setDestinationAccountNumber(l.getDestinationAccountNumber());
        return e;
    }

    @Override
    public Loan save(Loan loan) {
        return toModel(jpaRepository.save(toEntity(loan)));
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return jpaRepository.findById(id).map(this::toModel);
    }

    @Override
    public List<Loan> findByApplicantId(String applicantId) {
        return jpaRepository.findByApplicantId(applicantId).stream().map(this::toModel).collect(Collectors.toList());
    }
}
