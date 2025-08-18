package com.rca.mis.model.academic;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.teacher.Teacher;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "timetable_exceptions")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TimetableException extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private TimetableSlot timetableSlot;

    @Column(name = "exception_date", nullable = false)
    private LocalDate exceptionDate;

    @Column(name = "reason", columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "exception_type", nullable = false, length = 50)
    private ExceptionType exceptionType = ExceptionType.CANCELLED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substitute_teacher_id")
    private Teacher substituteTeacher;

    @Column(name = "new_start_time")
    private LocalTime newStartTime;

    @Column(name = "new_end_time")
    private LocalTime newEndTime;

    @Column(name = "new_room", length = 100)
    private String newRoom;

    @Column(name = "is_online")
    private Boolean isOnline = false;

    @Column(name = "online_platform")
    private String onlinePlatform;

    @Column(name = "online_link")
    private String onlineLink;

    @Column(name = "is_notified")
    private Boolean isNotified = false;

    @Column(name = "notification_sent_at")
    private java.time.LocalDateTime notificationSentAt;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_at")
    private java.time.LocalDateTime approvedAt;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // Helper methods
    public String getDisplayName() {
        return timetableSlot.getDisplayName() + " - " + exceptionType.getDisplayName() + 
               " on " + exceptionDate;
    }

    public boolean isCancelled() {
        return ExceptionType.CANCELLED.equals(exceptionType);
    }

    public boolean isRescheduled() {
        return ExceptionType.RESCHEDULED.equals(exceptionType);
    }

    public boolean isSubstituted() {
        return ExceptionType.SUBSTITUTED.equals(exceptionType);
    }

    public boolean isOnline() {
        return Boolean.TRUE.equals(isOnline);
    }

    public boolean isNotified() {
        return Boolean.TRUE.equals(isNotified);
    }

    public boolean hasSubstituteTeacher() {
        return substituteTeacher != null;
    }

    public boolean hasNewTime() {
        return newStartTime != null && newEndTime != null;
    }

    public boolean hasNewRoom() {
        return newRoom != null && !newRoom.trim().isEmpty();
    }

    public boolean isApproved() {
        return approvedAt != null;
    }

    public boolean hasOnlineDetails() {
        return isOnline() && onlinePlatform != null && onlineLink != null;
    }

    public enum ExceptionType {
        CANCELLED("Cancelled"),
        RESCHEDULED("Rescheduled"),
        SUBSTITUTED("Substituted"),
        ROOM_CHANGE("Room Change"),
        TIME_CHANGE("Time Change"),
        ONLINE_SESSION("Online Session"),
        FIELD_TRIP("Field Trip"),
        EXAM("Exam"),
        HOLIDAY("Holiday"),
        EMERGENCY("Emergency");

        private final String displayName;

        ExceptionType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
