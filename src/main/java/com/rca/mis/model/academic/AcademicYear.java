package com.rca.mis.model.academic;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.student.StudentAcademicRecord;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "academic_years")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AcademicYear extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private AcademicYearStatus status = AcademicYearStatus.PLANNING;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "academic_calendar", columnDefinition = "TEXT")
    private String academicCalendar;

    @Column(name = "holidays", columnDefinition = "TEXT")
    private String holidays;

    @Column(name = "exam_schedule", columnDefinition = "TEXT")
    private String examSchedule;

    @Column(name = "registration_deadline")
    private LocalDate registrationDeadline;

    @Column(name = "enrollment_deadline")
    private LocalDate enrollmentDeadline;

    @Column(name = "graduation_date")
    private LocalDate graduationDate;

    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent = false;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "academicYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Term> terms = new ArrayList<>();

    @OneToMany(mappedBy = "academicYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Class> classes = new ArrayList<>();

    @OneToMany(mappedBy = "academicYear", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentAcademicRecord> studentAcademicRecords = new ArrayList<>();

    // Helper methods
    public String getDisplayName() {
        return name + " (" + startDate.getYear() + "-" + endDate.getYear() + ")";
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    public boolean isCurrent() {
        return Boolean.TRUE.equals(isCurrent);
    }

    public boolean isPlanning() {
        return AcademicYearStatus.PLANNING.equals(status);
    }

    public boolean isActiveStatus() {
        return AcademicYearStatus.ACTIVE.equals(status);
    }

    public boolean isCompleted() {
        return AcademicYearStatus.COMPLETED.equals(status);
    }

    public boolean isArchived() {
        return AcademicYearStatus.ARCHIVED.equals(status);
    }

    public boolean isCancelled() {
        return AcademicYearStatus.CANCELLED.equals(status);
    }

    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }

    public boolean hasAcademicCalendar() {
        return academicCalendar != null && !academicCalendar.trim().isEmpty();
    }

    public boolean hasHolidays() {
        return holidays != null && !holidays.trim().isEmpty();
    }

    public boolean hasExamSchedule() {
        return examSchedule != null && !examSchedule.trim().isEmpty();
    }

    public boolean hasRegistrationDeadline() {
        return registrationDeadline != null;
    }

    public boolean hasEnrollmentDeadline() {
        return enrollmentDeadline != null;
    }

    public boolean hasGraduationDate() {
        return graduationDate != null;
    }

    public boolean hasNotes() {
        return notes != null && !notes.trim().isEmpty();
    }

    public boolean hasTerms() {
        return terms != null && !terms.isEmpty();
    }

    public boolean hasClasses() {
        return classes != null && !classes.isEmpty();
    }

    public boolean hasStudentRecords() {
        return studentAcademicRecords != null && !studentAcademicRecords.isEmpty();
    }

    public boolean isCurrentlyActive() {
        LocalDate now = LocalDate.now();
        return startDate.isBefore(now.plusDays(1)) && endDate.isAfter(now.minusDays(1));
    }

    public boolean isUpcoming() {
        LocalDate now = LocalDate.now();
        return startDate.isAfter(now);
    }

    public boolean isPast() {
        LocalDate now = LocalDate.now();
        return endDate.isBefore(now);
    }

    public int getDurationInDays() {
        return (int) (endDate.toEpochDay() - startDate.toEpochDay()) + 1;
    }

    public int getDurationInMonths() {
        return (int) java.time.Period.between(startDate, endDate).toTotalMonths();
    }

    public int getDurationInYears() {
        return (int) java.time.Period.between(startDate, endDate).getYears();
    }

    public boolean isRegistrationOpen() {
        if (!hasRegistrationDeadline()) return false;
        LocalDate now = LocalDate.now();
        return now.isBefore(registrationDeadline.plusDays(1));
    }

    public boolean isEnrollmentOpen() {
        if (!hasEnrollmentDeadline()) return false;
        LocalDate now = LocalDate.now();
        return now.isBefore(enrollmentDeadline.plusDays(1));
    }

    public boolean isGraduationScheduled() {
        if (!hasGraduationDate()) return false;
        LocalDate now = LocalDate.now();
        return graduationDate.isAfter(now);
    }

    public String getAcademicYearRange() {
        return startDate.getYear() + "-" + endDate.getYear();
    }

    public String getShortName() {
        return startDate.getYear() + "/" + endDate.getYear();
    }

    public boolean overlaps(AcademicYear other) {
        return !(endDate.isBefore(other.startDate) || startDate.isAfter(other.endDate));
    }

    public boolean contains(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean isCurrentAcademicYear() {
        LocalDate now = LocalDate.now();
        return contains(now);
    }

    public boolean isNextAcademicYear() {
        LocalDate now = LocalDate.now();
        return startDate.isAfter(now);
    }

    public boolean isPreviousAcademicYear() {
        LocalDate now = LocalDate.now();
        return endDate.isBefore(now);
    }
}
