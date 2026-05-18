package app.application.adapters.api.request;

public class TransferRequest {
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private double amount;

    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public void setSourceAccountNumber(String sourceAccountNumber) { this.sourceAccountNumber = sourceAccountNumber; }
    public String getTargetAccountNumber() { return targetAccountNumber; }
    public void setTargetAccountNumber(String targetAccountNumber) { this.targetAccountNumber = targetAccountNumber; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
