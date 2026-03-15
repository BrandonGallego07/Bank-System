package bank_model;

public class AuditLog {

    private String auditLogId;
    private String operationType;
    private String operationDateTime;
    private User executedBy;
    private String affectedProductId;
    private String detailData;

    public AuditLog() {
    }

    public AuditLog(String auditLogId, String operationType, String operationDateTime,
                    User executedBy, String affectedProductId, String detailData) {
        this.auditLogId = auditLogId;
        this.operationType = operationType;
        this.operationDateTime = operationDateTime;
        this.executedBy = executedBy;
        this.affectedProductId = affectedProductId;
        this.detailData = detailData;
    }

    public String getAuditLogId() {
        return auditLogId;
    }

    public void setAuditLogId(String auditLogId) {
        this.auditLogId = auditLogId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationDateTime() {
        return operationDateTime;
    }

    public void setOperationDateTime(String operationDateTime) {
        this.operationDateTime = operationDateTime;
    }

    public User getExecutedBy() {
        return executedBy;
    }

    public void setExecutedBy(User executedBy) {
        this.executedBy = executedBy;
    }

    public String getAffectedProductId() {
        return affectedProductId;
    }

    public void setAffectedProductId(String affectedProductId) {
        this.affectedProductId = affectedProductId;
    }

    public String getDetailData() {
        return detailData;
    }

    public void setDetailData(String detailData) {
        this.detailData = detailData;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "auditLogId='" + auditLogId + '\'' +
                ", operationType='" + operationType + '\'' +
                ", operationDateTime='" + operationDateTime + '\'' +
                ", executedBy=" + executedBy +
                ", affectedProductId='" + affectedProductId + '\'' +
                ", detailData='" + detailData + '\'' +
                '}';
    }
}