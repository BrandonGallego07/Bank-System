package app.application.adapters.persistence.sql.entities;

import app.domain.enums.TransferStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "transfers")
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sourceAccountNumber;
    private String targetAccountNumber;
    private double amount;
    private String creationDateTime;
    private String approvalDateTime;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    private Long createdByUserId;
    private Long approvedByUserId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSourceAccountNumber() { return sourceAccountNumber; }
    public void setSourceAccountNumber(String sourceAccountNumber) { this.sourceAccountNumber = sourceAccountNumber; }
    public String getTargetAccountNumber() { return targetAccountNumber; }
    public void setTargetAccountNumber(String targetAccountNumber) { this.targetAccountNumber = targetAccountNumber; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCreationDateTime() { return creationDateTime; }
    public void setCreationDateTime(String creationDateTime) { this.creationDateTime = creationDateTime; }
    public String getApprovalDateTime() { return approvalDateTime; }
    public void setApprovalDateTime(String approvalDateTime) { this.approvalDateTime = approvalDateTime; }
    public TransferStatus getStatus() { return status; }
    public void setStatus(TransferStatus status) { this.status = status; }
    public Long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Long createdByUserId) { this.createdByUserId = createdByUserId; }
    public Long getApprovedByUserId() { return approvedByUserId; }
    public void setApprovedByUserId(Long approvedByUserId) { this.approvedByUserId = approvedByUserId; }
}
