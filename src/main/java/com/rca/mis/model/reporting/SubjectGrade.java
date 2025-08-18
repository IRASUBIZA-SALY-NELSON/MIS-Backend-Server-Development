package com.rca.mis.model.reporting;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.academic.Subject;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "subject_grades")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SubjectGrade extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_card_id", nullable = false)
    private ReportCard reportCard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "score", columnDefinition = "DECIMAL(8,2)")
    private BigDecimal score;

    @Column(name = "max_score", columnDefinition = "DECIMAL(8,2)")
    private BigDecimal maxScore;

    @Column(name = "percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal percentage;

    @Column(name = "grade", length = 10)
    private String grade;

    @Column(name = "grade_point", columnDefinition = "DECIMAL(3,2)")
    private BigDecimal gradePoint;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "is_passing")
    private Boolean isPassing = true;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "teacher_comments", columnDefinition = "TEXT")
    private String teacherComments;

    @Column(name = "assessment_count")
    private Integer assessmentCount = 0;

    @Column(name = "attendance_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal attendancePercentage;

    @Column(name = "participation_score", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal participationScore;

    @Column(name = "homework_score", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal homeworkScore;

    @Column(name = "exam_score", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal examScore;

    @Column(name = "project_score", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal projectScore;

    // Helper methods
    public String getDisplayName() {
        return subject.getName() + " - " + score + "/" + maxScore;
    }

    public boolean isPassing() {
        return Boolean.TRUE.equals(isPassing);
    }

    public boolean hasScore() {
        return score != null;
    }

    public boolean hasGrade() {
        return grade != null && !grade.trim().isEmpty();
    }

    public boolean hasRank() {
        return rank != null;
    }

    public BigDecimal calculatePercentage() {
        if (score != null && maxScore != null && maxScore.compareTo(BigDecimal.ZERO) > 0) {
            return score.divide(maxScore, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return null;
    }

    public boolean isExcellent() {
        if (percentage != null) {
            return percentage.compareTo(new BigDecimal("80")) >= 0;
        }
        return "A".equals(grade) || "A+".equals(grade) || "A-".equals(grade);
    }

    public boolean isGood() {
        if (percentage != null) {
            return percentage.compareTo(new BigDecimal("60")) >= 0 && 
                   percentage.compareTo(new BigDecimal("79")) <= 0;
        }
        return "B".equals(grade) || "B+".equals(grade) || "B-".equals(grade);
    }

    public boolean isAverage() {
        if (percentage != null) {
            return percentage.compareTo(new BigDecimal("40")) >= 0 && 
                   percentage.compareTo(new BigDecimal("59")) <= 0;
        }
        return "C".equals(grade) || "C+".equals(grade) || "C-".equals(grade);
    }

    public boolean isBelowAverage() {
        if (percentage != null) {
            return percentage.compareTo(new BigDecimal("40")) < 0;
        }
        return "D".equals(grade) || "D+".equals(grade) || "D-".equals(grade);
    }

    public boolean isFailing() {
        if (percentage != null) {
            return percentage.compareTo(new BigDecimal("40")) < 0;
        }
        return "F".equals(grade) || "E".equals(grade);
    }

    public String getGradeCategory() {
        if (isExcellent()) return "Excellent";
        if (isGood()) return "Good";
        if (isAverage()) return "Average";
        if (isBelowAverage()) return "Below Average";
        if (isFailing()) return "Failing";
        return "Unknown";
    }
}
