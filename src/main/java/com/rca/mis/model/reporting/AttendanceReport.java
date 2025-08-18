package com.rca.mis.model.reporting;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.academic.Class;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_reports")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttendanceReport extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class clazz;

    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @Column(name = "total_students", nullable = false)
    private Integer totalStudents;

    @Column(name = "present_count", nullable = false)
    private Integer presentCount = 0;

    @Column(name = "absent_count", nullable = false)
    private Integer absentCount = 0;

    @Column(name = "late_count", nullable = false)
    private Integer lateCount = 0;

    @Column(name = "excused_count", nullable = false)
    private Integer excusedCount = 0;

    @Column(name = "unexcused_count", nullable = false)
    private Integer unexcusedCount = 0;

    @Column(name = "half_day_count", nullable = false)
    private Integer halfDayCount = 0;

    @Column(name = "attendance_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal attendancePercentage;

    @Column(name = "absenteeism_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal absenteeismPercentage;

    @Column(name = "late_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal latePercentage;

    @Column(name = "excused_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal excusedPercentage;

    @Column(name = "unexcused_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal unexcusedPercentage;

    @Column(name = "half_day_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal halfDayPercentage;

    @Column(name = "report_type", length = 50)
    private String reportType;

    @Column(name = "generated_by")
    private String generatedBy;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "is_published")
    private Boolean isPublished = false;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadata;

    // Helper methods
    public String getDisplayName() {
        return clazz.getName() + " - " + reportDate + " (" + 
               presentCount + "/" + totalStudents + ")";
    }

    public boolean isPublished() {
        return Boolean.TRUE.equals(isPublished);
    }

    public boolean hasData() {
        return totalStudents != null && totalStudents > 0;
    }

    public boolean isComplete() {
        return (presentCount + absentCount + lateCount + excusedCount + 
                unexcusedCount + halfDayCount) == totalStudents;
    }

    public BigDecimal calculateAttendancePercentage() {
        if (totalStudents != null && totalStudents > 0) {
            return new BigDecimal(presentCount)
                    .divide(new BigDecimal(totalStudents), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return null;
    }

    public BigDecimal calculateAbsenteeismPercentage() {
        if (totalStudents != null && totalStudents > 0) {
            return new BigDecimal(absentCount)
                    .divide(new BigDecimal(totalStudents), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return null;
    }

    public BigDecimal calculateLatePercentage() {
        if (totalStudents != null && totalStudents > 0) {
            return new BigDecimal(lateCount)
                    .divide(new BigDecimal(totalStudents), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return null;
    }

    public BigDecimal calculateExcusedPercentage() {
        if (totalStudents != null && totalStudents > 0) {
            return new BigDecimal(excusedCount)
                    .divide(new BigDecimal(totalStudents), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return null;
    }

    public BigDecimal calculateUnexcusedPercentage() {
        if (totalStudents != null && totalStudents > 0) {
            return new BigDecimal(unexcusedCount)
                    .divide(new BigDecimal(totalStudents), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return null;
    }

    public BigDecimal calculateHalfDayPercentage() {
        if (totalStudents != null && totalStudents > 0) {
            return new BigDecimal(halfDayCount)
                    .divide(new BigDecimal(totalStudents), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return null;
    }

    public boolean isGoodAttendance() {
        return attendancePercentage != null && attendancePercentage.compareTo(new BigDecimal("90")) >= 0;
    }

    public boolean isConcerningAttendance() {
        return attendancePercentage != null && attendancePercentage.compareTo(new BigDecimal("80")) < 0;
    }

    public boolean isCriticalAttendance() {
        return attendancePercentage != null && attendancePercentage.compareTo(new BigDecimal("70")) < 0;
    }
}
