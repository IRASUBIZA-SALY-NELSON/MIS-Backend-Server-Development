package com.rca.mis.model.teacher;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.academic.AcademicYear;
import com.rca.mis.model.academic.Class;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "teacher_classes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"teacher_id", "class_id", "academic_year_id"})
})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TeacherClass extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class clazz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_class_teacher", nullable = false)
    private Boolean isClassTeacher = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // Helper methods
    public String getDisplayName() {
        return teacher.getUser().getProfile().getFullName() + " - " + clazz.getName();
    }

    public boolean isCurrentlyTeaching() {
        LocalDate now = LocalDate.now();
        return startDate.isBefore(now.plusDays(1)) && 
               (endDate == null || endDate.isAfter(now.minusDays(1)));
    }

    public boolean isClassTeacher() {
        return Boolean.TRUE.equals(isClassTeacher);
    }

    public boolean hasEndDate() {
        return endDate != null;
    }
}
