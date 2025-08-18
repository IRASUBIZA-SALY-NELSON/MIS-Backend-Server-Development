package com.rca.mis.model.academic;

import com.rca.mis.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "grade_scales")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GradeScale extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "min_percentage", nullable = false, columnDefinition = "DECIMAL(5,2)")
    private BigDecimal minPercentage;

    @Column(name = "max_percentage", nullable = false, columnDefinition = "DECIMAL(5,2)")
    private BigDecimal maxPercentage;

    @Column(name = "grade", nullable = false, length = 10)
    private String grade;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "grade_point", columnDefinition = "DECIMAL(3,2)")
    private BigDecimal gradePoint;

    @Column(name = "is_passing", nullable = false)
    private Boolean isPassing = true;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "color_code", length = 7)
    private String colorCode;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    // Helper methods
    public String getDisplayName() {
        return grade + " (" + minPercentage + "%-" + maxPercentage + "%)";
    }

    public boolean isPassing() {
        return Boolean.TRUE.equals(isPassing);
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    public boolean isInRange(BigDecimal percentage) {
        if (percentage == null) return false;
        return percentage.compareTo(minPercentage) >= 0 && 
               percentage.compareTo(maxPercentage) <= 0;
    }

    public boolean isExcellent() {
        return "A".equals(grade) || "A+".equals(grade) || "A-".equals(grade);
    }

    public boolean isGood() {
        return "B".equals(grade) || "B+".equals(grade) || "B-".equals(grade);
    }

    public boolean isAverage() {
        return "C".equals(grade) || "C+".equals(grade) || "C-".equals(grade);
    }

    public boolean isBelowAverage() {
        return "D".equals(grade) || "D+".equals(grade) || "D-".equals(grade);
    }

    public boolean isFailing() {
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
