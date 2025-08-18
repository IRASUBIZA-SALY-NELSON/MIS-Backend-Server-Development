package com.rca.mis.model.academic;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.reporting.PerformanceReport;
import com.rca.mis.model.reporting.ReportCard;
import com.rca.mis.model.student.StudentAcademicRecord;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "terms")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Term extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private TermStatus status = TermStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assessment> assessments = new ArrayList<>();

    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentAcademicRecord> studentAcademicRecords = new ArrayList<>();

    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReportCard> reportCards = new ArrayList<>();

    @OneToMany(mappedBy = "term", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerformanceReport> performanceReports = new ArrayList<>();

    // Helper methods
    public boolean isCurrentTerm() {
        LocalDate now = LocalDate.now();
        return startDate.isBefore(now.plusDays(1)) && endDate.isAfter(now.minusDays(1));
    }

    public int getDurationInDays() {
        return (int) (endDate.toEpochDay() - startDate.toEpochDay()) + 1;
    }

    public boolean isOverlapping(Term other) {
        return !(endDate.isBefore(other.startDate) || startDate.isAfter(other.endDate));
    }

    public enum TermStatus {
        PENDING("Pending"),
        ACTIVE("Active"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");

        private final String displayName;

        TermStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
