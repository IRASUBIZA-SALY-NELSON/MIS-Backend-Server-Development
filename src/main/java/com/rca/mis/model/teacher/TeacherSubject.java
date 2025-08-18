package com.rca.mis.model.teacher;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.academic.Subject;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teacher_subjects", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"teacher_id", "subject_id"})
})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TeacherSubject extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name = "expertise_level", length = 50)
    private String expertiseLevel;

    @Column(name = "years_experience")
    private Integer yearsExperience;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // Helper methods
    public String getDisplayName() {
        return teacher.getUser().getProfile().getFullName() + " - " + subject.getName();
    }

    public boolean isPrimarySubject() {
        return Boolean.TRUE.equals(isPrimary);
    }

    public boolean hasExpertise() {
        return expertiseLevel != null && !expertiseLevel.trim().isEmpty();
    }
}
