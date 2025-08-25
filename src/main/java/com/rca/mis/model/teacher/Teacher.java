package com.rca.mis.model.teacher;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.academic.AcademicYear;
import com.rca.mis.model.academic.Assessment;
import com.rca.mis.model.academic.Class;
import com.rca.mis.model.academic.Subject;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"user", "teacherSubjects", "teacherClasses", "assessments", "gradedMarks"})
public class Teacher extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private com.rca.mis.model.user.User user;

    @Column(name = "employee_id", nullable = false, length = 50, unique = true)
    private String employeeId;

    @Column(name = "qualification", length = 100)
    private String qualification;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "specialization", length = 100)
    private String specialization;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private TeacherStatus status = TeacherStatus.ACTIVE;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "faculty", length = 100)
    private String faculty;

    @Column(name = "office_location", length = 100)
    private String officeLocation;

    @Column(name = "office_hours", length = 200)
    private String officeHours;

    @Column(name = "phone_extension", length = 20)
    private String phoneExtension;

    @Column(name = "emergency_contact", length = 20)
    private String emergencyContact;

    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_relationship", length = 50)
    private String emergencyContactRelationship;

    @Column(name = "is_mentor", nullable = false)
    private Boolean isMentor = false;

    @Column(name = "is_advisor", nullable = false)
    private Boolean isAdvisor = false;

    @Column(name = "is_coordinator", nullable = false)
    private Boolean isCoordinator = false;

    @Column(name = "is_head_of_department", nullable = false)
    private Boolean isHeadOfDepartment = false;

    @Column(name = "is_dean", nullable = false)
    private Boolean isDean = false;

    @Column(name = "is_principal", nullable = false)
    private Boolean isPrincipal = false;

    @Column(name = "max_classes")
    private Integer maxClasses;

    @Column(name = "current_classes")
    private Integer currentClasses = 0;

    @Column(name = "max_students")
    private Integer maxStudents;

    @Column(name = "current_students")
    private Integer currentStudents = 0;

    @Column(name = "salary_grade", length = 20)
    private String salaryGrade;

    @Column(name = "contract_type", length = 50)
    private String contractType;

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;

    @Column(name = "is_full_time", nullable = false)
    private Boolean isFullTime = true;

    @Column(name = "working_hours_per_week")
    private Integer workingHoursPerWeek;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeacherSubject> teacherSubjects = new ArrayList<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeacherClass> teacherClasses = new ArrayList<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assessment> assessments = new ArrayList<>();

    @OneToMany(mappedBy = "gradedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<com.rca.mis.model.student.StudentMarks> gradedMarks = new ArrayList<>();

    // Helper methods
    public String getDisplayName() {
        return user.getProfile().getFullName() + " (" + employeeId + ")";
    }

    public boolean isActive() {
        return TeacherStatus.ACTIVE.equals(status);
    }

    public boolean isInactive() {
        return TeacherStatus.INACTIVE.equals(status);
    }

    public boolean isSuspended() {
        return TeacherStatus.SUSPENDED.equals(status);
    }

    public boolean isOnLeave() {
        return TeacherStatus.ON_LEAVE.equals(status);
    }

    public boolean isTerminated() {
        return TeacherStatus.TERMINATED.equals(status);
    }

    public boolean isRetired() {
        return TeacherStatus.RETIRED.equals(status);
    }

    public boolean isProbation() {
        return TeacherStatus.PROBATION.equals(status);
    }

    public boolean isPending() {
        return TeacherStatus.PENDING.equals(status);
    }

    public boolean isContract() {
        return TeacherStatus.CONTRACT.equals(status);
    }

    public boolean isPartTime() {
        return TeacherStatus.PART_TIME.equals(status);
    }

    public boolean isMentor() {
        return Boolean.TRUE.equals(isMentor);
    }

    public boolean isAdvisor() {
        return Boolean.TRUE.equals(isAdvisor);
    }

    public boolean isCoordinator() {
        return Boolean.TRUE.equals(isCoordinator);
    }

    public boolean isHeadOfDepartment() {
        return Boolean.TRUE.equals(isHeadOfDepartment);
    }

    public boolean isDean() {
        return Boolean.TRUE.equals(isDean);
    }

    public boolean isPrincipal() {
        return Boolean.TRUE.equals(isPrincipal);
    }

    public boolean isFullTime() {
        return Boolean.TRUE.equals(isFullTime);
    }

    public boolean hasDepartment() {
        return department != null && !department.trim().isEmpty();
    }

    public boolean hasFaculty() {
        return faculty != null && !faculty.trim().isEmpty();
    }

    public boolean hasOfficeLocation() {
        return officeLocation != null && !officeLocation.trim().isEmpty();
    }

    public boolean hasOfficeHours() {
        return officeHours != null && !officeHours.trim().isEmpty();
    }

    public boolean hasPhoneExtension() {
        return phoneExtension != null && !phoneExtension.trim().isEmpty();
    }

    public boolean hasEmergencyContact() {
        return emergencyContact != null && emergencyContactName != null;
    }

    public boolean hasQualification() {
        return qualification != null && !qualification.trim().isEmpty();
    }

    public boolean hasExperience() {
        return experienceYears != null && experienceYears > 0;
    }

    public boolean hasSpecialization() {
        return specialization != null && !specialization.trim().isEmpty();
    }

    public boolean hasSalaryGrade() {
        return salaryGrade != null && !salaryGrade.trim().isEmpty();
    }

    public boolean hasContractType() {
        return contractType != null && !contractType.trim().isEmpty();
    }

    public boolean hasContractPeriod() {
        return contractStartDate != null;
    }

    public boolean isContractActive() {
        if (!hasContractPeriod()) return true;
        
        LocalDate now = LocalDate.now();
        return now.isAfter(contractStartDate.minusDays(1)) &&
               (contractEndDate == null || now.isBefore(contractEndDate.plusDays(1)));
    }

    public boolean hasWorkingHours() {
        return workingHoursPerWeek != null && workingHoursPerWeek > 0;
    }

    public boolean hasNotes() {
        return notes != null && !notes.trim().isEmpty();
    }

    public boolean hasSubjects() {
        return teacherSubjects != null && !teacherSubjects.isEmpty();
    }

    public boolean hasClasses() {
        return teacherClasses != null && !teacherClasses.isEmpty();
    }

    public boolean hasAssessments() {
        return assessments != null && !assessments.isEmpty();
    }

    public boolean hasGradedMarks() {
        return gradedMarks != null && !gradedMarks.isEmpty();
    }

    public boolean isAtMaxClasses() {
        return maxClasses != null && currentClasses != null && currentClasses >= maxClasses;
    }

    public boolean isAtMaxStudents() {
        return maxStudents != null && currentStudents != null && currentStudents >= maxStudents;
    }

    public boolean canTakeMoreClasses() {
        return maxClasses == null || currentClasses == null || currentClasses < maxClasses;
    }

    public boolean canTakeMoreStudents() {
        return maxStudents == null || currentStudents == null || currentStudents < maxStudents;
    }

    public Integer getAvailableClassSlots() {
        if (maxClasses != null && currentClasses != null) {
            return Math.max(0, maxClasses - currentClasses);
        }
        return null;
    }

    public Integer getAvailableStudentSlots() {
        if (maxStudents != null && currentStudents != null) {
            return Math.max(0, maxStudents - currentStudents);
        }
        return null;
    }

    public boolean isSeniorTeacher() {
        return experienceYears != null && experienceYears >= 10;
    }

    public boolean isJuniorTeacher() {
        return experienceYears != null && experienceYears < 5;
    }

    public boolean isMidLevelTeacher() {
        return experienceYears != null && experienceYears >= 5 && experienceYears < 10;
    }

    public String getExperienceLevel() {
        if (isSeniorTeacher()) return "Senior";
        if (isMidLevelTeacher()) return "Mid-Level";
        if (isJuniorTeacher()) return "Junior";
        return "Unknown";
    }

    public boolean isCurrentlyEmployed() {
        LocalDate now = LocalDate.now();
        return hireDate.isBefore(now.plusDays(1)) &&
               (contractEndDate == null || now.isBefore(contractEndDate.plusDays(1)));
    }

    public int getYearsOfService() {
        if (hireDate != null) {
            return (int) java.time.Period.between(hireDate, LocalDate.now()).getYears();
        }
        return 0;
    }
}
