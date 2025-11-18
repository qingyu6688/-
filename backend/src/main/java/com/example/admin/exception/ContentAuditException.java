package com.example.admin.exception;

/**
 * 内容审核异常
 * 用于传递审核失败的详细信息
 */
public class ContentAuditException extends RuntimeException {
    
    private String auditReason;
    
    public ContentAuditException(String message) {
        super(message);
        this.auditReason = message;
    }
    
    public ContentAuditException(String message, String auditReason) {
        super(message);
        this.auditReason = auditReason;
    }
    
    public String getAuditReason() {
        return auditReason;
    }
}
