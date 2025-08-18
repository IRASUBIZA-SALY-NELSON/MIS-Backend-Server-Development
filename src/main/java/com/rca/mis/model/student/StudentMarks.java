package com.rca.mis.model.student;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.academic.Assessment;
import com.rca.mis.model.teacher.Teacher;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_marks", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "assessment_id"})
})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentMarks extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @Column(name = "marks_obtained", columnDefinition = "DECIMAL(8,2)")
    private BigDecimal marksObtained;

    @Column(name = "percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal percentage;

    @Column(name = "grade", length = 10)
    private String grade;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graded_by")
    private Teacher gradedBy;

    @Column(name = "graded_date")
    private LocalDateTime gradedDate;

    @Column(name = "submission_date")
    private LocalDateTime submissionDate;

    @Column(name = "is_late_submission")
    private Boolean isLateSubmission = false;

    @Column(name = "late_penalty_applied", columnDefinition = "DECIMAL(8,2)")
    private BigDecimal latePenaltyApplied;

    @Column(name = "attempt_number")
    private Integer attemptNumber = 1;

    @Column(name = "time_taken_minutes")
    private Integer timeTakenMinutes;

    @Column(name = "is_absent")
    private Boolean isAbsent = false;

    @Column(name = "absence_reason", columnDefinition = "TEXT")
    private String absenceReason;

    @Column(name = "is_excused")
    private Boolean isExcused = false;

    @Column(name = "excuse_reason", columnDefinition = "TEXT")
    private String excuseReason;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    // Helper methods
    public String getDisplayName() {
        return student.getUser().getProfile().getFullName() + " - " + 
               assessment.getName();
    }

    public boolean isGraded() {
        return marksObtained != null;
    }

    public boolean isAbsent() {
        return Boolean.TRUE.equals(isAbsent);
    }

    public boolean isLateSubmission() {
        return Boolean.TRUE.equals(isLateSubmission);
    }

    public boolean isExcused() {
        return Boolean.TRUE.equals(isExcused);
    }

    public boolean hasFeedback() {
        return feedback != null && !feedback.trim().isEmpty();
    }

    public BigDecimal calculatePercentage() {
        if (marksObtained != null && assessment.getTotalMarks() != null && 
            assessment.getTotalMarks().compareTo(BigDecimal.ZERO) > 0) {
            return marksObtained.divide(assessment.getTotalMarks(), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return null;
    }

    public boolean isPassing(BigDecimal passingPercentage) {
        if (percentage != null && passingPercentage != null) {
            return percentage.compareTo(passingPercentage) >= 0;
        }
        return false;
    }

    public boolean isExcellent(BigDecimal excellentPercentage) {
        if (percentage != null && excellentPercentage != null) {
            return percentage.compareTo(excellentPercentage) >= 0;
        }
        return false;
    }
}
