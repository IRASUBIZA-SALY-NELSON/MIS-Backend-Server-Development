package com.rca.mis.model.communication;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Message extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 50)
    private MessageType messageType = MessageType.PERSONAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 50)
    private MessagePriority priority = MessagePriority.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private MessageStatus status = MessageStatus.SENT;

    @Column(name = "sent_date", nullable = false)
    private LocalDateTime sentDate;

    @Column(name = "read_date")
    private LocalDateTime readDate;

    @Column(name = "reply_to_message_id")
    private String replyToMessageId;

    @Column(name = "is_important")
    private Boolean isImportant = false;

    @Column(name = "is_archived")
    private Boolean isArchived = false;

    @Column(name = "archived_date")
    private LocalDateTime archivedDate;

    @Column(name = "deleted_by_sender")
    private Boolean deletedBySender = false;

    @Column(name = "deleted_by_recipient")
    private Boolean deletedByRecipient = false;

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageAttachment> attachments = new ArrayList<>();

    // Helper methods
    public String getDisplayName() {
        return subject + " - " + sender.getProfile().getFullName() + " → " + 
               recipient.getProfile().getFullName();
    }

    public boolean isRead() {
        return readDate != null;
    }

    public boolean isUnread() {
        return readDate == null;
    }

    public boolean isImportant() {
        return Boolean.TRUE.equals(isImportant);
    }

    public boolean isArchived() {
        return Boolean.TRUE.equals(isArchived);
    }

    public boolean isDeletedBySender() {
        return Boolean.TRUE.equals(deletedBySender);
    }

    public boolean isDeletedByRecipient() {
        return Boolean.TRUE.equals(deletedByRecipient);
    }

    public boolean isReply() {
        return replyToMessageId != null;
    }

    public boolean hasAttachments() {
        return attachments != null && !attachments.isEmpty();
    }

    public boolean isHighPriority() {
        return MessagePriority.HIGH.equals(priority) || MessagePriority.URGENT.equals(priority);
    }

    public enum MessageType {
        PERSONAL("Personal"),
        ANNOUNCEMENT("Announcement"),
        NOTIFICATION("Notification"),
        SYSTEM("System"),
        GROUP("Group");

        private final String displayName;

        MessageType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum MessagePriority {
        LOW("Low"),
        NORMAL("Normal"),
        HIGH("High"),
        URGENT("Urgent");

        private final String displayName;

        MessagePriority(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum MessageStatus {
        DRAFT("Draft"),
        SENT("Sent"),
        DELIVERED("Delivered"),
        READ("Read"),
        FAILED("Failed");

        private final String displayName;

        MessageStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
