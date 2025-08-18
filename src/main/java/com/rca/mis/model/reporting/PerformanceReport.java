package com.rca.mis.model.reporting;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.academic.AcademicYear;
import com.rca.mis.model.academic.Class;
import com.rca.mis.model.academic.Subject;
import com.rca.mis.model.academic.Term;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "performance_reports")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PerformanceReport extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Class clazz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private Term term;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id")
    private AcademicYear academicYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false, length = 50)
    private ReportType reportType;

    @Column(name = "generated_date", nullable = false)
    private LocalDateTime generatedDate;

    @Column(name = "total_students")
    private Integer totalStudents;

    @Column(name = "participating_students")
    private Integer participatingStudents;

    @Column(name = "average_score", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal averageScore;

    @Column(name = "highest_score", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal highestScore;

    @Column(name = "lowest_score", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal lowestScore;

    @Column(name = "median_score", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal medianScore;

    @Column(name = "standard_deviation", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal standardDeviation;

    @Column(name = "passing_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal passingPercentage;

    @Column(name = "excellent_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal excellentPercentage;

    @Column(name = "good_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal goodPercentage;

    @Column(name = "average_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal averagePercentage;

    @Column(name = "below_average_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal belowAveragePercentage;

    @Column(name = "failing_percentage", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal failingPercentage;

    @Column(name = "grade_distribution", columnDefinition = "JSONB")
    private String gradeDistribution;

    @Column(name = "score_distribution", columnDefinition = "JSONB")
    private String scoreDistribution;

    @Column(name = "subject_rankings", columnDefinition = "JSONB")
    private String subjectRankings;

    @Column(name = "class_rankings", columnDefinition = "JSONB")
    private String classRankings;

    @Column(name = "trends", columnDefinition = "JSONB")
    private String trends;

    @Column(name = "recommendations", columnDefinition = "TEXT")
    private String recommendations;

    @Column(name = "is_published")
    private Boolean isPublished = false;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadata;

    // Helper methods
    public String getDisplayName() {
        StringBuilder name = new StringBuilder();
        if (clazz != null) name.append(clazz.getName()).append(" - ");
        if (subject != null) name.append(subject.getName()).append(" - ");
        if (term != null) name.append(term.getName()).append(" - ");
        if (academicYear != null) name.append(academicYear.getName()).append(" - ");
        name.append(reportType.getDisplayName());
        return name.toString();
    }

    public boolean isPublished() {
        return Boolean.TRUE.equals(isPublished);
    }

    public boolean isClassReport() {
        return clazz != null && subject == null;
    }

    public boolean isSubjectReport() {
        return subject != null && clazz == null;
    }

    public boolean isComprehensiveReport() {
        return clazz != null && subject != null;
    }

    public boolean hasStatisticalData() {
        return averageScore != null || highestScore != null || lowestScore != null;
    }

    public boolean hasDistributionData() {
        return gradeDistribution != null || scoreDistribution != null;
    }

    public boolean hasRankingData() {
        return subjectRankings != null || classRankings != null;
    }

    public BigDecimal calculatePassingPercentage() {
        if (totalStudents != null && totalStudents > 0 && participatingStudents != null) {
            return new BigDecimal(participatingStudents)
                    .divide(new BigDecimal(totalStudents), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return null;
    }

    public enum ReportType {
        CLASS_PERFORMANCE("Class Performance"),
        SUBJECT_PERFORMANCE("Subject Performance"),
        COMPREHENSIVE("Comprehensive"),
        COMPARATIVE("Comparative"),
        TREND_ANALYSIS("Trend Analysis"),
        BENCHMARK("Benchmark"),
        CUSTOM("Custom");

        private final String displayName;

        ReportType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
