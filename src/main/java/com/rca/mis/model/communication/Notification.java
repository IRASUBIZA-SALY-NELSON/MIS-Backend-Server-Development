package com.rca.mis.model.communication;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private NotificationType type = NotificationType.INFO;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 50)
    private NotificationPriority priority = NotificationPriority.NORMAL;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "action_url")
    private String actionUrl;

    @Column(name = "action_text", length = 100)
    private String actionText;

    @Column(name = "entity_type", length = 100)
    private String entityType;

    @Column(name = "entity_id")
    private String entityId;

    @Column(name = "is_dismissed")
    private Boolean isDismissed = false;

    @Column(name = "dismissed_at")
    private LocalDateTime dismissedAt;

    @Column(name = "is_sent_email")
    private Boolean isSentEmail = false;

    @Column(name = "is_sent_sms")
    private Boolean isSentSms = false;

    @Column(name = "is_sent_push")
    private Boolean isSentPush = false;

    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadata;

    // Helper methods
    public String getDisplayName() {
        return title + " - " + user.getProfile().getFullName();
    }

    public boolean isRead() {
        return Boolean.TRUE.equals(isRead);
    }

    public boolean isUnread() {
        return !isRead();
    }

    public boolean isDismissed() {
        return Boolean.TRUE.equals(isDismissed);
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isActive() {
        return !isExpired() && !isDismissed();
    }

    public boolean hasAction() {
        return actionUrl != null && !actionUrl.trim().isEmpty();
    }

    public boolean isHighPriority() {
        return NotificationPriority.HIGH.equals(priority) || 
               NotificationPriority.URGENT.equals(priority);
    }

    public boolean isSentViaEmail() {
        return Boolean.TRUE.equals(isSentEmail);
    }

    public boolean isSentViaSms() {
        return Boolean.TRUE.equals(isSentSms);
    }

    public boolean isSentViaPush() {
        return Boolean.TRUE.equals(isSentPush);
    }

    public enum NotificationType {
        INFO("Information"),
        SUCCESS("Success"),
        WARNING("Warning"),
        ERROR("Error"),
        REMINDER("Reminder"),
        ALERT("Alert"),
        UPDATE("Update"),
        SYSTEM("System");

        private final String displayName;

        NotificationType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum NotificationPriority {
        LOW("Low"),
        NORMAL("Normal"),
        HIGH("High"),
        URGENT("Urgent");

        private final String displayName;

        NotificationPriority(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
