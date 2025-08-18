package com.rca.mis.model.student;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.academic.AcademicYear;
import com.rca.mis.model.academic.Class;
import com.rca.mis.model.user.Guardian;
import com.rca.mis.model.user.Parent;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Student extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private com.rca.mis.model.user.User user;

    @Column(name = "student_code", nullable = false, length = 50, unique = true)
    private String studentCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_class_id")
    private Class currentClass;

    @Column(name = "admission_date", nullable = false)
    private LocalDate admissionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guardian_id")
    private Guardian guardian;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private StudentStatus status = StudentStatus.ACTIVE;

    @Column(name = "enrollment_number", length = 50)
    private String enrollmentNumber;

    @Column(name = "roll_number", length = 20)
    private String rollNumber;

    @Column(name = "previous_school", length = 200)
    private String previousSchool;

    @Column(name = "previous_grade", length = 20)
    private String previousGrade;

    @Column(name = "transfer_date")
    private LocalDate transferDate;

    @Column(name = "graduation_date")
    private LocalDate graduationDate;

    @Column(name = "is_international", nullable = false)
    private Boolean isInternational = false;

    @Column(name = "nationality", length = 50)
    private String nationality;

    @Column(name = "passport_number", length = 50)
    private String passportNumber;

    @Column(name = "visa_number", length = 50)
    private String visaNumber;

    @Column(name = "visa_expiry_date")
    private LocalDate visaExpiryDate;

    @Column(name = "is_scholarship", nullable = false)
    private Boolean isScholarship = false;

    @Column(name = "scholarship_type", length = 100)
    private String scholarshipType;

    @Column(name = "scholarship_amount", columnDefinition = "DECIMAL(10,2)")
    private java.math.BigDecimal scholarshipAmount;

    @Column(name = "is_financial_aid", nullable = false)
    private Boolean isFinancialAid = false;

    @Column(name = "financial_aid_type", length = 100)
    private String financialAidType;

    @Column(name = "financial_aid_amount", columnDefinition = "DECIMAL(10,2)")
    private java.math.BigDecimal financialAidAmount;

    @Column(name = "is_special_needs", nullable = false)
    private Boolean isSpecialNeeds = false;

    @Column(name = "special_needs_details", columnDefinition = "TEXT")
    private String specialNeedsDetails;

    @Column(name = "is_boarding", nullable = false)
    private Boolean isBoarding = false;

    @Column(name = "boarding_house", length = 100)
    private String boardingHouse;

    @Column(name = "room_number", length = 20)
    private String roomNumber;

    @Column(name = "is_transport", nullable = false)
    private Boolean isTransport = false;

    @Column(name = "transport_route", length = 100)
    private String transportRoute;

    @Column(name = "transport_stop", length = 100)
    private String transportStop;

    @Column(name = "emergency_contact", length = 20)
    private String emergencyContact;

    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_relationship", length = 50)
    private String emergencyContactRelationship;

    @Column(name = "medical_conditions", columnDefinition = "TEXT")
    private String medicalConditions;

    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies;

    @Column(name = "medications", columnDefinition = "TEXT")
    private String medications;

    @Column(name = "blood_group", length = 10)
    private String bloodGroup;

    @Column(name = "height_cm")
    private Integer heightCm;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentAcademicRecord> academicRecords = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentAttendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentProject> projects = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<com.rca.mis.model.student.StudentMarks> marks = new ArrayList<>();

    // Helper methods
    public String getDisplayName() {
        return user.getProfile().getFullName() + " (" + studentCode + ")";
    }

    public boolean isActive() {
        return StudentStatus.ACTIVE.equals(status);
    }

    public boolean isInactive() {
        return StudentStatus.INACTIVE.equals(status);
    }

    public boolean isSuspended() {
        return StudentStatus.SUSPENDED.equals(status);
    }

    public boolean isGraduated() {
        return StudentStatus.GRADUATED.equals(status);
    }

    public boolean isTransferred() {
        return StudentStatus.TRANSFERRED.equals(status);
    }

    public boolean isWithdrawn() {
        return StudentStatus.WITHDRAWN.equals(status);
    }

    public boolean isExpelled() {
        return StudentStatus.EXPELLED.equals(status);
    }

    public boolean isOnLeave() {
        return StudentStatus.ON_LEAVE.equals(status);
    }

    public boolean isPending() {
        return StudentStatus.PENDING.equals(status);
    }

    public boolean isProbation() {
        return StudentStatus.PROBATION.equals(status);
    }

    public boolean isInternational() {
        return Boolean.TRUE.equals(isInternational);
    }

    public boolean isScholarship() {
        return Boolean.TRUE.equals(isScholarship);
    }

    public boolean isFinancialAid() {
        return Boolean.TRUE.equals(isFinancialAid);
    }

    public boolean isSpecialNeeds() {
        return Boolean.TRUE.equals(isSpecialNeeds);
    }

    public boolean isBoarding() {
        return Boolean.TRUE.equals(isBoarding);
    }

    public boolean isTransport() {
        return Boolean.TRUE.equals(isTransport);
    }

    public boolean hasCurrentClass() {
        return currentClass != null;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public boolean hasGuardian() {
        return guardian != null;
    }

    public boolean hasEnrollmentNumber() {
        return enrollmentNumber != null && !enrollmentNumber.trim().isEmpty();
    }

    public boolean hasRollNumber() {
        return rollNumber != null && !rollNumber.trim().isEmpty();
    }

    public boolean hasPreviousSchool() {
        return previousSchool != null && !previousSchool.trim().isEmpty();
    }

    public boolean hasPreviousGrade() {
        return previousGrade != null && !previousGrade.trim().isEmpty();
    }

    public boolean hasTransferDate() {
        return transferDate != null;
    }

    public boolean hasGraduationDate() {
        return graduationDate != null;
    }

    public boolean hasNationality() {
        return nationality != null && !nationality.trim().isEmpty();
    }

    public boolean hasPassportNumber() {
        return passportNumber != null && !passportNumber.trim().isEmpty();
    }

    public boolean hasVisaNumber() {
        return visaNumber != null && !visaNumber.trim().isEmpty();
    }

    public boolean hasVisaExpiryDate() {
        return visaExpiryDate != null;
    }

    public boolean hasScholarshipType() {
        return scholarshipType != null && !scholarshipType.trim().isEmpty();
    }

    public boolean hasScholarshipAmount() {
        return scholarshipAmount != null;
    }

    public boolean hasFinancialAidType() {
        return financialAidType != null && !financialAidType.trim().isEmpty();
    }

    public boolean hasFinancialAidAmount() {
        return financialAidAmount != null;
    }

    public boolean hasSpecialNeedsDetails() {
        return specialNeedsDetails != null && !specialNeedsDetails.trim().isEmpty();
    }

    public boolean hasBoardingHouse() {
        return boardingHouse != null && !boardingHouse.trim().isEmpty();
    }

    public boolean hasRoomNumber() {
        return roomNumber != null && !roomNumber.trim().isEmpty();
    }

    public boolean hasTransportRoute() {
        return transportRoute != null && !transportRoute.trim().isEmpty();
    }

    public boolean hasTransportStop() {
        return transportStop != null && !transportStop.trim().isEmpty();
    }

    public boolean hasEmergencyContact() {
        return emergencyContact != null && emergencyContactName != null;
    }

    public boolean hasMedicalConditions() {
        return medicalConditions != null && !medicalConditions.trim().isEmpty();
    }

    public boolean hasAllergies() {
        return allergies != null && !allergies.trim().isEmpty();
    }

    public boolean hasMedications() {
        return medications != null && !medications.trim().isEmpty();
    }

    public boolean hasBloodGroup() {
        return bloodGroup != null && !bloodGroup.trim().isEmpty();
    }

    public boolean hasHeight() {
        return heightCm != null && heightCm > 0;
    }

    public boolean hasWeight() {
        return weightKg != null && weightKg > 0;
    }

    public boolean hasNotes() {
        return notes != null && !notes.trim().isEmpty();
    }

    public boolean hasAcademicRecords() {
        return academicRecords != null && !academicRecords.isEmpty();
    }

    public boolean hasAttendances() {
        return attendances != null && !attendances.isEmpty();
    }

    public boolean hasProjects() {
        return projects != null && !projects.isEmpty();
    }

    public boolean hasMarks() {
        return marks != null && !marks.isEmpty();
    }

    public boolean isVisaExpired() {
        if (!hasVisaExpiryDate()) return false;
        return LocalDate.now().isAfter(visaExpiryDate);
    }

    public boolean isVisaExpiringSoon() {
        if (!hasVisaExpiryDate()) return false;
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysFromNow = now.plusDays(30);
        return visaExpiryDate.isBefore(thirtyDaysFromNow) && visaExpiryDate.isAfter(now);
    }

    public int getAge() {
        if (user != null && user.getProfile() != null && user.getProfile().getDateOfBirth() != null) {
            return java.time.Period.between(user.getProfile().getDateOfBirth(), LocalDate.now()).getYears();
        }
        return 0;
    }

    public int getYearsEnrolled() {
        if (admissionDate != null) {
            return java.time.Period.between(admissionDate, LocalDate.now()).getYears();
        }
        return 0;
    }

    public boolean isCurrentlyEnrolled() {
        return isActive() && hasCurrentClass();
    }

    public boolean canGraduate() {
        return isActive() && hasCurrentClass() && 
               currentClass.getAcademicYear() != null && 
               currentClass.getAcademicYear().isActive();
    }

    public String getCurrentGrade() {
        if (hasCurrentClass()) {
            return currentClass.getLevel();
        }
        return "Not Assigned";
    }

    public String getCurrentAcademicYear() {
        if (hasCurrentClass() && currentClass.getAcademicYear() != null) {
            return currentClass.getAcademicYear().getName();
        }
        return "Not Assigned";
    }
}
