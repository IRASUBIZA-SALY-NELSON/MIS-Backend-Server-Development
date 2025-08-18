package com.rca.mis.model.academic;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.student.StudentMarks;
import com.rca.mis.model.teacher.Teacher;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assessments")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Assessment extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private AssessmentType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class clazz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(name = "total_marks", nullable = false, columnDefinition = "DECIMAL(8,2)")
    private BigDecimal totalMarks;

    @Column(name = "weight", nullable = false, columnDefinition = "DECIMAL(5,2)")
    private BigDecimal weight;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = false;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "submission_deadline")
    private LocalDateTime submissionDeadline;

    @Column(name = "allow_late_submission")
    private Boolean allowLateSubmission = false;

    @Column(name = "late_submission_penalty", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal lateSubmissionPenalty;

    @Column(name = "max_attempts")
    private Integer maxAttempts = 1;

    @Column(name = "time_limit_minutes")
    private Integer timeLimitMinutes;

    @Column(name = "is_online")
    private Boolean isOnline = false;

    @Column(name = "online_platform")
    private String onlinePlatform;

    @Column(name = "room")
    private String room;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentMarks> studentMarks = new ArrayList<>();

    // Helper methods
    public String getDisplayName() {
        return name + " - " + subject.getName() + " - " + clazz.getName();
    }

    public boolean isPublished() {
        return Boolean.TRUE.equals(isPublished);
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    public boolean isOnline() {
        return Boolean.TRUE.equals(isOnline);
    }

    public boolean hasTimeLimit() {
        return timeLimitMinutes != null && timeLimitMinutes > 0;
    }

    public boolean allowsMultipleAttempts() {
        return maxAttempts != null && maxAttempts > 1;
    }

    public boolean isOverdue() {
        return dueDate != null && LocalDate.now().isAfter(dueDate);
    }

    public boolean isCurrentlyActive() {
        LocalDate now = LocalDate.now();
        return (startDate == null || !now.isBefore(startDate)) &&
               (endDate == null || !now.isAfter(endDate));
    }

    public boolean allowsLateSubmission() {
        return Boolean.TRUE.equals(allowLateSubmission);
    }

    public enum AssessmentType {
        QUIZ("Quiz"),
        TEST("Test"),
        EXAM("Exam"),
        ASSIGNMENT("Assignment"),
        PROJECT("Project"),
        PRESENTATION("Presentation"),
        PRACTICAL("Practical"),
        HOMEWORK("Homework"),
        MIDTERM("Midterm"),
        FINAL("Final");

        private final String displayName;

        AssessmentType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
