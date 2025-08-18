package com.rca.mis.model.user;

import com.rca.mis.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guardians")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Guardian extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "relationship", nullable = false, length = 50)
    private String relationship;

    @Column(name = "contact_info", columnDefinition = "TEXT")
    private String contactInfo;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "is_legal_guardian", nullable = false)
    private Boolean isLegalGuardian = false;

    @Column(name = "is_temporary_guardian", nullable = false)
    private Boolean isTemporaryGuardian = false;

    @Column(name = "guardianship_start_date")
    private java.time.LocalDate guardianshipStartDate;

    @Column(name = "guardianship_end_date")
    private java.time.LocalDate guardianshipEndDate;

    @Column(name = "guardianship_document", length = 255)
    private String guardianshipDocument;

    @Column(name = "is_emergency_contact", nullable = false)
    private Boolean isEmergencyContact = false;

    @Column(name = "is_authorized_pickup", nullable = false)
    private Boolean isAuthorizedPickup = false;

    @Column(name = "is_financial_guardian", nullable = false)
    private Boolean isFinancialGuardian = false;

    @Column(name = "is_educational_guardian", nullable = false)
    private Boolean isEducationalGuardian = false;

    @Column(name = "preferred_contact_method", length = 50)
    private String preferredContactMethod;

    @Column(name = "preferred_contact_time", length = 50)
    private String preferredContactTime;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "guardian", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentParent> studentParents = new ArrayList<>();

    // Helper methods
    public String getDisplayName() {
        return user.getProfile().getFullName() + " (" + relationship + ")";
    }

    public boolean isLegalGuardian() {
        return Boolean.TRUE.equals(isLegalGuardian);
    }

    public boolean isTemporaryGuardian() {
        return Boolean.TRUE.equals(isTemporaryGuardian);
    }

    public boolean isEmergencyContact() {
        return Boolean.TRUE.equals(isEmergencyContact);
    }

    public boolean isAuthorizedPickup() {
        return Boolean.TRUE.equals(isAuthorizedPickup);
    }

    public boolean isFinancialGuardian() {
        return Boolean.TRUE.equals(isFinancialGuardian);
    }

    public boolean isEducationalGuardian() {
        return Boolean.TRUE.equals(isEducationalGuardian);
    }

    public boolean hasGuardianshipPeriod() {
        return guardianshipStartDate != null;
    }

    public boolean isGuardianshipActive() {
        if (!hasGuardianshipPeriod()) return false;
        
        java.time.LocalDate now = java.time.LocalDate.now();
        return now.isAfter(guardianshipStartDate.minusDays(1)) &&
               (guardianshipEndDate == null || now.isBefore(guardianshipEndDate.plusDays(1)));
    }

    public boolean hasGuardianshipDocument() {
        return guardianshipDocument != null && !guardianshipDocument.trim().isEmpty();
    }

    public boolean hasContactInformation() {
        return contactInfo != null && !contactInfo.trim().isEmpty();
    }

    public boolean hasAddress() {
        return address != null && !address.trim().isEmpty();
    }

    public boolean hasPreferredContact() {
        return preferredContactMethod != null || preferredContactTime != null;
    }

    public boolean hasStudents() {
        return studentParents != null && !studentParents.isEmpty();
    }
}
