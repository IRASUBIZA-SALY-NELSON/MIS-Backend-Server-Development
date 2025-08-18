package com.rca.mis.model.system;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuditLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false, length = 50)
    private ActionType actionType = ActionType.OTHER;

    @Column(name = "entity_type", length = 100)
    private String entityType;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "old_values", columnDefinition = "JSONB")
    private String oldValues;

    @Column(name = "new_values", columnDefinition = "JSONB")
    private String newValues;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "request_url", length = 500)
    private String requestUrl;

    @Column(name = "request_method", length = 10)
    private String requestMethod;

    @Column(name = "response_status")
    private Integer responseStatus;

    @Column(name = "execution_time_ms")
    private Long executionTimeMs;

    @Column(name = "is_successful", nullable = false)
    private Boolean isSuccessful = true;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;

    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadata;

    @Column(name = "severity", length = 20)
    private String severity;

    @Column(name = "tags", length = 500)
    private String tags;

    // Helper methods
    public String getDisplayName() {
        return action + " - " + (user != null ? user.getProfile().getFullName() : "System") + 
               " - " + timestamp;
    }

    public boolean isSuccessful() {
        return Boolean.TRUE.equals(isSuccessful);
    }

    public boolean isError() {
        return !isSuccessful();
    }

    public boolean hasUser() {
        return user != null;
    }

    public boolean hasEntity() {
        return entityType != null && entityId != null;
    }

    public boolean hasChanges() {
        return oldValues != null || newValues != null;
    }

    public boolean hasOldValues() {
        return oldValues != null && !oldValues.trim().isEmpty();
    }

    public boolean hasNewValues() {
        return newValues != null && !newValues.trim().isEmpty();
    }

    public boolean hasErrorDetails() {
        return errorMessage != null || stackTrace != null;
    }

    public boolean hasPerformanceData() {
        return executionTimeMs != null;
    }

    public boolean isHighSeverity() {
        return "HIGH".equals(severity) || "CRITICAL".equals(severity);
    }

    public boolean isMediumSeverity() {
        return "MEDIUM".equals(severity);
    }

    public boolean isLowSeverity() {
        return "LOW".equals(severity) || severity == null;
    }

    public boolean isCreateAction() {
        return ActionType.CREATE.equals(actionType);
    }

    public boolean isUpdateAction() {
        return ActionType.UPDATE.equals(actionType);
    }

    public boolean isDeleteAction() {
        return ActionType.DELETE.equals(actionType);
    }

    public boolean isReadAction() {
        return ActionType.READ.equals(actionType);
    }

    public boolean isLoginAction() {
        return ActionType.LOGIN.equals(actionType);
    }

    public boolean isLogoutAction() {
        return ActionType.LOGOUT.equals(actionType);
    }

    public boolean isSecurityAction() {
        return ActionType.SECURITY.equals(actionType);
    }

    public enum ActionType {
        CREATE("Create"),
        READ("Read"),
        UPDATE("Update"),
        DELETE("Delete"),
        LOGIN("Login"),
        LOGOUT("Logout"),
        SECURITY("Security"),
        SYSTEM("System"),
        EXPORT("Export"),
        IMPORT("Import"),
        BACKUP("Backup"),
        RESTORE("Restore"),
        CONFIGURATION("Configuration"),
        OTHER("Other");

        private final String displayName;

        ActionType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
