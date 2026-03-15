package bank_model;

import bank_enums.LoanStatus;
import bank_enums.LoanType;

public class Loan {

    private int loanId;
    private LoanType loanType;
    private Customer applicant;
    private double requestedAmount;
    private double approvedAmount;
    private double interestRate;
    private int termMonths;
    private LoanStatus status;
    private String approvalDate;
    private String disbursementDate;
    private Account destinationAccount;

    public Loan() {
    }

    public Loan(int loanId, LoanType loanType, Customer applicant, double requestedAmount,
                double approvedAmount, double interestRate, int termMonths,
                LoanStatus status, String approvalDate, String disbursementDate,
                Account destinationAccount) {
        this.loanId = loanId;
        this.loanType = loanType;
        this.applicant = applicant;
        this.requestedAmount = requestedAmount;
        this.approvedAmount = approvedAmount;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        this.status = status;
        this.approvalDate = approvalDate;
        this.disbursementDate = disbursementDate;
        this.destinationAccount = destinationAccount;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public Customer getApplicant() {
        return applicant;
    }

    public void setApplicant(Customer applicant) {
        this.applicant = applicant;
    }

    public double getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public double getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(double approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(int termMonths) {
        this.termMonths = termMonths;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(String disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loanId=" + loanId +
                ", loanType=" + loanType +
                ", applicant=" + applicant +
                ", requestedAmount=" + requestedAmount +
                ", approvedAmount=" + approvedAmount +
                ", interestRate=" + interestRate +
                ", termMonths=" + termMonths +
                ", status=" + status +
                ", approvalDate='" + approvalDate + '\'' +
                ", disbursementDate='" + disbursementDate + '\'' +
                ", destinationAccount=" + destinationAccount +
                '}';
    }
}