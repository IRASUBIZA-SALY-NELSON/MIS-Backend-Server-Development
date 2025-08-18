package com.rca.mis.model.student;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.academic.AcademicYear;
import com.rca.mis.model.academic.Class;
import com.rca.mis.model.academic.Term;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "student_academic_records", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "academic_year_id", "term_id", "class_id"})
})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentAcademicRecord extends BaseEntity {

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

    @Column(name = "final_grade", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal finalGrade;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "total_marks", columnDefinition = "DECIMAL(8,2)")
    private BigDecimal totalMarks;

    @Column(name = "obtained_marks", columnDefinition = "DECIMAL(8,2)")
    private BigDecimal obtainedMarks;

    @Column(name = "percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal percentage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private AcademicRecordStatus status = AcademicRecordStatus.IN_PROGRESS;

    @Column(name = "promotion_date")
    private LocalDate promotionDate;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "is_promoted")
    private Boolean isPromoted = false;

    // Helper methods
    public String getDisplayName() {
        return student.getUser().getProfile().getFullName() + " - " + 
               academicYear.getName() + " - " + term.getName();
    }

    public boolean isCompleted() {
        return AcademicRecordStatus.COMPLETED.equals(status);
    }

    public boolean isPromoted() {
        return Boolean.TRUE.equals(isPromoted);
    }

    public boolean hasGrade() {
        return finalGrade != null;
    }

    public boolean hasRank() {
        return rank != null;
    }

    public BigDecimal calculatePercentage() {
        if (totalMarks != null && obtainedMarks != null && totalMarks.compareTo(BigDecimal.ZERO) > 0) {
            return obtainedMarks.divide(totalMarks, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return null;
    }

    public enum AcademicRecordStatus {
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed"),
        FAILED("Failed"),
        INCOMPLETE("Incomplete"),
        WITHDRAWN("Withdrawn");

        private final String displayName;

        AcademicRecordStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
