package com.rca.mis.model.user;

import com.rca.mis.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parents")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"user", "studentParents"})
public class Parent extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "relationship", nullable = false, length = 50)
    private String relationship;

    @Column(name = "occupation", length = 100)
    private String occupation;

    @Column(name = "employer", length = 100)
    private String employer;

    @Column(name = "work_phone", length = 20)
    private String workPhone;

    @Column(name = "emergency_contact", length = 20)
    private String emergencyContact;

    @Column(name = "emergency_contact_relationship", length = 50)
    private String emergencyContactRelationship;

    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "is_primary_contact", nullable = false)
    private Boolean isPrimaryContact = false;

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

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentParent> studentParents = new ArrayList<>();

    // Helper methods
    public String getDisplayName() {
        return user.getProfile().getFullName() + " (" + relationship + ")";
    }

    public boolean isPrimaryContact() {
        return Boolean.TRUE.equals(isPrimaryContact);
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

    public boolean hasWorkInformation() {
        return occupation != null || employer != null || workPhone != null;
    }

    public boolean hasEmergencyContact() {
        return emergencyContact != null && emergencyContactName != null;
    }

    public boolean hasPreferredContact() {
        return preferredContactMethod != null || preferredContactTime != null;
    }

    public boolean hasStudents() {
        return studentParents != null && !studentParents.isEmpty();
    }
}
