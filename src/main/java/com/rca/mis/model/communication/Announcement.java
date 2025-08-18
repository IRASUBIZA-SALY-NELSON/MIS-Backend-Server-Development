package com.rca.mis.model.communication;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "announcements")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Announcement extends BaseEntity {

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_audience", nullable = false, length = 50)
    private TargetAudience targetAudience = TargetAudience.ALL;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private AnnouncementStatus status = AnnouncementStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 50)
    private AnnouncementPriority priority = AnnouncementPriority.NORMAL;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "featured_until")
    private LocalDateTime featuredUntil;

    @Column(name = "is_pinned")
    private Boolean isPinned = false;

    @Column(name = "pinned_until")
    private LocalDateTime pinnedUntil;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "is_public")
    private Boolean isPublic = true;

    @Column(name = "requires_acknowledgment")
    private Boolean requiresAcknowledgment = false;

    @Column(name = "acknowledgment_deadline")
    private LocalDateTime acknowledgmentDeadline;

    @ElementCollection
    @CollectionTable(name = "announcement_tags", joinColumns = @JoinColumn(name = "announcement_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadata;

    // Helper methods
    public String getDisplayName() {
        return title + " - " + createdBy.getProfile().getFullName();
    }

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        return (startDate == null || !now.isBefore(startDate)) &&
               (endDate == null || !now.isAfter(endDate));
    }

    public boolean isExpired() {
        return endDate != null && LocalDate.now().isAfter(endDate);
    }

    public boolean isFeatured() {
        return Boolean.TRUE.equals(isFeatured);
    }

    public boolean isPinned() {
        return Boolean.TRUE.equals(isPinned);
    }

    public boolean isPublic() {
        return Boolean.TRUE.equals(isPublic);
    }

    public boolean requiresAcknowledgment() {
        return Boolean.TRUE.equals(requiresAcknowledgment);
    }

    public boolean isAcknowledgmentOverdue() {
        return requiresAcknowledgment() && acknowledgmentDeadline != null &&
               LocalDateTime.now().isAfter(acknowledgmentDeadline);
    }

    public boolean isHighPriority() {
        return AnnouncementPriority.HIGH.equals(priority) || 
               AnnouncementPriority.URGENT.equals(priority);
    }

    public boolean hasTags() {
        return tags != null && !tags.isEmpty();
    }

    public enum TargetAudience {
        ALL("All Users"),
        STUDENTS("Students Only"),
        TEACHERS("Teachers Only"),
        ADMINISTRATORS("Administrators Only"),
        PARENTS("Parents Only"),
        GUARDIANS("Guardians Only"),
        STAFF("Staff Only"),
        SPECIFIC_CLASS("Specific Class"),
        SPECIFIC_GROUP("Specific Group");

        private final String displayName;

        TargetAudience(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum AnnouncementStatus {
        DRAFT("Draft"),
        PUBLISHED("Published"),
        ARCHIVED("Archived"),
        CANCELLED("Cancelled");

        private final String displayName;

        AnnouncementStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum AnnouncementPriority {
        LOW("Low"),
        NORMAL("Normal"),
        HIGH("High"),
        URGENT("Urgent");

        private final String displayName;

        AnnouncementPriority(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
