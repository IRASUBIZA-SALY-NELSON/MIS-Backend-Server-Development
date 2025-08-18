package com.rca.mis.model.user;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.student.Student;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "student_parents", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "parent_id"}),
    @UniqueConstraint(columnNames = {"student_id", "guardian_id"})
})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentParent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guardian_id")
    private Guardian guardian;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_type", nullable = false, length = 50)
    private RelationshipType relationshipType = RelationshipType.PARENT;

    @Column(name = "relationship_start_date")
    private LocalDate relationshipStartDate;

    @Column(name = "relationship_end_date")
    private LocalDate relationshipEndDate;

    @Column(name = "is_primary_contact", nullable = false)
    private Boolean isPrimaryContact = false;

    @Column(name = "is_emergency_contact", nullable = false)
    private Boolean isEmergencyContact = false;

    @Column(name = "is_authorized_pickup", nullable = false)
    private Boolean isAuthorizedPickup = false;

    @Column(name = "is_financial_responsible", nullable = false)
    private Boolean isFinancialResponsible = false;

    @Column(name = "is_educational_responsible", nullable = false)
    private Boolean isEducationalResponsible = false;

    @Column(name = "contact_priority")
    private Integer contactPriority;

    @Column(name = "preferred_contact_method", length = 50)
    private String preferredContactMethod;

    @Column(name = "preferred_contact_time", length = 50)
    private String preferredContactTime;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // Helper methods
    public String getDisplayName() {
        if (parent != null) {
            return student.getUser().getProfile().getFullName() + " - " + 
                   parent.getUser().getProfile().getFullName() + " (" + relationshipType.getDisplayName() + ")";
        } else if (guardian != null) {
            return student.getUser().getProfile().getFullName() + " - " + 
                   guardian.getUser().getProfile().getFullName() + " (" + relationshipType.getDisplayName() + ")";
        }
        return "Unknown Relationship";
    }

    public boolean isParentRelationship() {
        return RelationshipType.PARENT.equals(relationshipType);
    }

    public boolean isGuardianRelationship() {
        return RelationshipType.GUARDIAN.equals(relationshipType);
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

    public boolean isFinancialResponsible() {
        return Boolean.TRUE.equals(isFinancialResponsible);
    }

    public boolean isEducationalResponsible() {
        return Boolean.TRUE.equals(isEducationalResponsible);
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    public boolean hasRelationshipPeriod() {
        return relationshipStartDate != null;
    }

    public boolean isRelationshipActive() {
        if (!hasRelationshipPeriod()) return true;
        
        LocalDate now = LocalDate.now();
        return now.isAfter(relationshipStartDate.minusDays(1)) &&
               (relationshipEndDate == null || now.isBefore(relationshipEndDate.plusDays(1)));
    }

    public boolean hasParent() {
        return parent != null;
    }

    public boolean hasGuardian() {
        return guardian != null;
    }

    public boolean hasPreferredContact() {
        return preferredContactMethod != null || preferredContactTime != null;
    }

    public boolean hasContactPriority() {
        return contactPriority != null;
    }

    public enum RelationshipType {
        PARENT("Parent"),
        GUARDIAN("Guardian"),
        STEP_PARENT("Step Parent"),
        ADOPTIVE_PARENT("Adoptive Parent"),
        FOSTER_PARENT("Foster Parent"),
        LEGAL_GUARDIAN("Legal Guardian"),
        TEMPORARY_GUARDIAN("Temporary Guardian");

        private final String displayName;

        RelationshipType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
