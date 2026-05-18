package app.domain.models;

import app.domain.enums.LoanStatus;
import app.domain.enums.LoanType;

public class Loan {
    private Long id;
    private LoanType loanType;
    private String applicantId;
    private double requestedAmount;
    private double approvedAmount;
    private double interestRate;
    private int termMonths;
    private LoanStatus status;
    private String approvalDate;
    private String disbursementDate;
    private String destinationAccountNumber;

    public Loan() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LoanType getLoanType() { return loanType; }
    public void setLoanType(LoanType loanType) { this.loanType = loanType; }
    public String getApplicantId() { return applicantId; }
    public void setApplicantId(String applicantId) { this.applicantId = applicantId; }
    public double getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(double requestedAmount) { this.requestedAmount = requestedAmount; }
    public double getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(double approvedAmount) { this.approvedAmount = approvedAmount; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public int getTermMonths() { return termMonths; }
    public void setTermMonths(int termMonths) { this.termMonths = termMonths; }
    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }
    public String getApprovalDate() { return approvalDate; }
    public void setApprovalDate(String approvalDate) { this.approvalDate = approvalDate; }
    public String getDisbursementDate() { return disbursementDate; }
    public void setDisbursementDate(String disbursementDate) { this.disbursementDate = disbursementDate; }
    public String getDestinationAccountNumber() { return destinationAccountNumber; }
    public void setDestinationAccountNumber(String destinationAccountNumber) { this.destinationAccountNumber = destinationAccountNumber; }
}
