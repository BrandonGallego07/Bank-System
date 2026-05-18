package app.domain.models;

import java.util.Map;

public class AuditLog {
    private String id;
    private String operationType;
    private String operationDateTime;
    private Long userId;
    private String userRole;
    private String affectedProductId;
    private Map<String, Object> detailData;

    public AuditLog() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public String getOperationDateTime() { return operationDateTime; }
    public void setOperationDateTime(String operationDateTime) { this.operationDateTime = operationDateTime; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public String getAffectedProductId() { return affectedProductId; }
    public void setAffectedProductId(String affectedProductId) { this.affectedProductId = affectedProductId; }
    public Map<String, Object> getDetailData() { return detailData; }
    public void setDetailData(Map<String, Object> detailData) { this.detailData = detailData; }
}
