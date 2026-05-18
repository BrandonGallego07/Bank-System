package app.application.adapters.api.request;

import app.domain.enums.LoanType;

public class LoanRequest {
    private LoanType loanType;
    private double requestedAmount;
    private double interestRate;
    private int termMonths;
    private String destinationAccountNumber;

    public LoanType getLoanType() { return loanType; }
    public void setLoanType(LoanType loanType) { this.loanType = loanType; }
    public double getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(double requestedAmount) { this.requestedAmount = requestedAmount; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
    public int getTermMonths() { return termMonths; }
    public void setTermMonths(int termMonths) { this.termMonths = termMonths; }
    public String getDestinationAccountNumber() { return destinationAccountNumber; }
    public void setDestinationAccountNumber(String destinationAccountNumber) { this.destinationAccountNumber = destinationAccountNumber; }
}
