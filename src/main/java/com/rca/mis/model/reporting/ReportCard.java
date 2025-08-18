package com.rca.mis.model.reporting;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.academic.AcademicYear;
import com.rca.mis.model.academic.Class;
import com.rca.mis.model.academic.Term;
import com.rca.mis.model.student.Student;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report_cards", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "academic_year_id", "term_id", "class_id"})
})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportCard extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class clazz;

    @Column(name = "generated_date", nullable = false)
    private LocalDateTime generatedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ReportCardStatus status = ReportCardStatus.DRAFT;

    @Column(name = "total_marks", columnDefinition = "DECIMAL(8,2)")
    private BigDecimal totalMarks;

    @Column(name = "obtained_marks", columnDefinition = "DECIMAL(8,2)")
    private BigDecimal obtainedMarks;

    @Column(name = "percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal percentage;

    @Column(name = "grade", length = 10)
    private String grade;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "total_subjects")
    private Integer totalSubjects;

    @Column(name = "passed_subjects")
    private Integer passedSubjects;

    @Column(name = "failed_subjects")
    private Integer failedSubjects;

    @Column(name = "average_score", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal averageScore;

    @Column(name = "highest_score", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal highestScore;

    @Column(name = "lowest_score", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal lowestScore;

    @Column(name = "attendance_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal attendancePercentage;

    @Column(name = "total_days")
    private Integer totalDays;

    @Column(name = "present_days")
    private Integer presentDays;

    @Column(name = "absent_days")
    private Integer absentDays;

    @Column(name = "late_days")
    private Integer lateDays;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "teacher_comments", columnDefinition = "TEXT")
    private String teacherComments;

    @Column(name = "parent_comments", columnDefinition = "TEXT")
    private String parentComments;

    @Column(name = "is_published")
    private Boolean isPublished = false;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "is_acknowledged")
    private Boolean isAcknowledged = false;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Column(name = "acknowledged_by")
    private String acknowledgedBy;

    @OneToMany(mappedBy = "reportCard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubjectGrade> subjectGrades = new ArrayList<>();

    // Helper methods
    public String getDisplayName() {
        return student.getUser().getProfile().getFullName() + " - " + 
               academicYear.getName() + " - " + term.getName();
    }

    public boolean isPublished() {
        return Boolean.TRUE.equals(isPublished);
    }

    public boolean isAcknowledged() {
        return Boolean.TRUE.equals(isAcknowledged);
    }

    public boolean isDraft() {
        return ReportCardStatus.DRAFT.equals(status);
    }

    public boolean isFinal() {
        return ReportCardStatus.FINAL.equals(status);
    }

    public boolean hasGrades() {
        return subjectGrades != null && !subjectGrades.isEmpty();
    }

    public boolean isPassing() {
        return percentage != null && percentage.compareTo(new BigDecimal("40")) >= 0;
    }

    public boolean isExcellent() {
        return percentage != null && percentage.compareTo(new BigDecimal("80")) >= 0;
    }

    public BigDecimal calculatePercentage() {
        if (totalMarks != null && obtainedMarks != null && totalMarks.compareTo(BigDecimal.ZERO) > 0) {
            return obtainedMarks.divide(totalMarks, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return null;
    }

    public BigDecimal calculateAverageScore() {
        if (hasGrades()) {
            BigDecimal total = subjectGrades.stream()
                    .map(SubjectGrade::getScore)
                    .filter(score -> score != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            if (total.compareTo(BigDecimal.ZERO) > 0) {
                return total.divide(new BigDecimal(subjectGrades.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
        }
        return null;
    }

    public enum ReportCardStatus {
        DRAFT("Draft"),
        GENERATED("Generated"),
        REVIEWED("Reviewed"),
        FINAL("Final"),
        PUBLISHED("Published"),
        ARCHIVED("Archived");

        private final String displayName;

        ReportCardStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
