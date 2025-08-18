package com.rca.mis.model.academic;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.student.StudentAttendance;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Subject extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "code", nullable = false, length = 20, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private SubjectCategory category = SubjectCategory.CORE;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "credits", nullable = false)
    private Integer credits;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "prerequisites", columnDefinition = "TEXT")
    private String prerequisites;

    @Column(name = "learning_objectives", columnDefinition = "TEXT")
    private String learningObjectives;

    @Column(name = "syllabus", columnDefinition = "TEXT")
    private String syllabus;

    @Column(name = "textbooks", columnDefinition = "TEXT")
    private String textbooks;

    @Column(name = "reference_materials", columnDefinition = "TEXT")
    private String referenceMaterials;

    @Column(name = "assessment_methods", columnDefinition = "TEXT")
    private String assessmentMethods;

    @Column(name = "passing_criteria", columnDefinition = "TEXT")
    private String passingCriteria;

    @Column(name = "max_students")
    private Integer maxStudents;

    @Column(name = "min_students")
    private Integer minStudents;

    @Column(name = "is_compulsory", nullable = false)
    private Boolean isCompulsory = true;

    @Column(name = "is_theory", nullable = false)
    private Boolean isTheory = true;

    @Column(name = "is_practical", nullable = false)
    private Boolean isPractical = false;

    @Column(name = "is_laboratory", nullable = false)
    private Boolean isLaboratory = false;

    @Column(name = "is_project_based", nullable = false)
    private Boolean isProjectBased = false;

    @Column(name = "weekly_hours")
    private Integer weeklyHours;

    @Column(name = "theory_hours")
    private Integer theoryHours;

    @Column(name = "practical_hours")
    private Integer practicalHours;

    @Column(name = "laboratory_hours")
    private Integer laboratoryHours;

    @Column(name = "semester", length = 20)
    private String semester;

    @Column(name = "academic_level", length = 50)
    private String academicLevel;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "faculty", length = 100)
    private String faculty;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassSubject> classSubjects = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assessment> assessments = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentAttendance> studentAttendances = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimetableSlot> timetableSlots = new ArrayList<>();

    // Helper methods
    public String getDisplayName() {
        return name + " (" + code + ")";
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    public boolean isCompulsory() {
        return Boolean.TRUE.equals(isCompulsory);
    }

    public boolean isTheory() {
        return Boolean.TRUE.equals(isTheory);
    }

    public boolean isPractical() {
        return Boolean.TRUE.equals(isPractical);
    }

    public boolean isLaboratory() {
        return Boolean.TRUE.equals(isLaboratory);
    }

    public boolean isProjectBased() {
        return Boolean.TRUE.equals(isProjectBased);
    }

    public boolean hasPrerequisites() {
        return prerequisites != null && !prerequisites.trim().isEmpty();
    }

    public boolean hasLearningObjectives() {
        return learningObjectives != null && !learningObjectives.trim().isEmpty();
    }

    public boolean hasSyllabus() {
        return syllabus != null && !syllabus.trim().isEmpty();
    }

    public boolean hasTextbooks() {
        return textbooks != null && !textbooks.trim().isEmpty();
    }

    public boolean hasReferenceMaterials() {
        return referenceMaterials != null && !referenceMaterials.trim().isEmpty();
    }

    public boolean hasAssessmentMethods() {
        return assessmentMethods != null && !assessmentMethods.trim().isEmpty();
    }

    public boolean hasPassingCriteria() {
        return passingCriteria != null && !passingCriteria.trim().isEmpty();
    }

    public boolean hasStudentLimits() {
        return maxStudents != null || minStudents != null;
    }

    public boolean hasWeeklyHours() {
        return weeklyHours != null && weeklyHours > 0;
    }

    public boolean hasTheoryHours() {
        return theoryHours != null && theoryHours > 0;
    }

    public boolean hasPracticalHours() {
        return practicalHours != null && practicalHours > 0;
    }

    public boolean hasLaboratoryHours() {
        return laboratoryHours != null && laboratoryHours > 0;
    }

    public boolean hasDepartment() {
        return department != null && !department.trim().isEmpty();
    }

    public boolean hasFaculty() {
        return faculty != null && !faculty.trim().isEmpty();
    }

    public boolean isCoreSubject() {
        return SubjectCategory.CORE.equals(category);
    }

    public boolean isElectiveSubject() {
        return SubjectCategory.ELECTIVE.equals(category);
    }

    public boolean isRequiredSubject() {
        return SubjectCategory.REQUIRED.equals(category);
    }

    public boolean isOptionalSubject() {
        return SubjectCategory.OPTIONAL.equals(category);
    }

    public boolean isTechnicalSubject() {
        return SubjectCategory.TECHNICAL.equals(category);
    }

    public boolean isTheoreticalSubject() {
        return SubjectCategory.THEORETICAL.equals(category);
    }

    public boolean isPracticalSubject() {
        return SubjectCategory.PRACTICAL.equals(category);
    }

    public boolean isLaboratorySubject() {
        return SubjectCategory.LABORATORY.equals(category);
    }

    public boolean isWorkshopSubject() {
        return SubjectCategory.WORKSHOP.equals(category);
    }

    public boolean isProjectSubject() {
        return SubjectCategory.PROJECT.equals(category);
    }

    public boolean isInternshipSubject() {
        return SubjectCategory.INTERNSHIP.equals(category);
    }

    public boolean isThesisSubject() {
        return SubjectCategory.THESIS.equals(category);
    }

    public boolean isSeminarSubject() {
        return SubjectCategory.SEMINAR.equals(category);
    }

    public boolean isTutorialSubject() {
        return SubjectCategory.TUTORIAL.equals(category);
    }

    public boolean isOtherSubject() {
        return SubjectCategory.OTHER.equals(category);
    }
}
