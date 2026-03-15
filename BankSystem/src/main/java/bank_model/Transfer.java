package bank_model;

import bank_enums.TransferStatus;

public class Transfer {

    private int transferId;
    private Account sourceAccount;
    private Account targetAccount;
    private double amount;
    private String creationDateTime;
    private String approvalDateTime;
    private TransferStatus status;
    private User createdBy;
    private User approvedBy;

    public Transfer() {
    }

    public Transfer(int transferId, Account sourceAccount, Account targetAccount, double amount,
                    String creationDateTime, String approvalDateTime, TransferStatus status,
                    User createdBy, User approvedBy) {
        this.transferId = transferId;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.creationDateTime = creationDateTime;
        this.approvalDateTime = approvalDateTime;
        this.status = status;
        this.createdBy = createdBy;
        this.approvedBy = approvedBy;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Account getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(Account targetAccount) {
        this.targetAccount = targetAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getApprovalDateTime() {
        return approvalDateTime;
    }

    public void setApprovalDateTime(String approvalDateTime) {
        this.approvalDateTime = approvalDateTime;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", sourceAccount=" + sourceAccount +
                ", targetAccount=" + targetAccount +
                ", amount=" + amount +
                ", creationDateTime='" + creationDateTime + '\'' +
                ", approvalDateTime='" + approvalDateTime + '\'' +
                ", status=" + status +
                ", createdBy=" + createdBy +
                ", approvedBy=" + approvedBy +
                '}';
    }
}