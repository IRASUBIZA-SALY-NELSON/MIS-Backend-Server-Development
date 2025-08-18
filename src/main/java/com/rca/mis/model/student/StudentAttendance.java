package com.rca.mis.model.student;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.academic.Class;
import com.rca.mis.model.academic.Subject;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "student_attendance", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "subject_id", "class_id", "date"})
})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentAttendance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class clazz;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private AttendanceStatus status = AttendanceStatus.PRESENT;

    @Column(name = "time_in")
    private LocalTime timeIn;

    @Column(name = "time_out")
    private LocalTime timeOut;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "is_excused")
    private Boolean isExcused = false;

    @Column(name = "excuse_reason", columnDefinition = "TEXT")
    private String excuseReason;

    @Column(name = "excuse_document")
    private String excuseDocument;

    @Column(name = "marked_by")
    private String markedBy;

    @Column(name = "marked_at")
    private LocalTime markedAt;

    // Helper methods
    public String getDisplayName() {
        return student.getUser().getProfile().getFullName() + " - " + 
               subject.getName() + " - " + date;
    }

    public boolean isPresent() {
        return AttendanceStatus.PRESENT.equals(status);
    }

    public boolean isAbsent() {
        return AttendanceStatus.ABSENT.equals(status);
    }

    public boolean isLate() {
        return AttendanceStatus.LATE.equals(status);
    }

    public boolean isExcused() {
        return Boolean.TRUE.equals(isExcused);
    }

    public boolean hasTimeIn() {
        return timeIn != null;
    }

    public boolean hasTimeOut() {
        return timeOut != null;
    }

    public boolean isFullDay() {
        return hasTimeIn() && hasTimeOut();
    }

    public enum AttendanceStatus {
        PRESENT("Present"),
        ABSENT("Absent"),
        LATE("Late"),
        HALF_DAY("Half Day"),
        EXCUSED("Excused"),
        UNEXCUSED("Unexcused");

        private final String displayName;

        AttendanceStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
