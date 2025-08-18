package com.rca.mis.model.academic;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.student.StudentAttendance;
import com.rca.mis.model.teacher.Teacher;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "class_subjects", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"class_id", "subject_id"})
})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClassSubject extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Class clazz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(name = "is_compulsory", nullable = false)
    private Boolean isCompulsory = true;

    @Column(name = "credits", nullable = false)
    private Integer credits;

    @Column(name = "weekly_hours", nullable = false)
    private Integer weeklyHours;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Note: Assessments are linked directly to class and subject, not to class_subject
    // @OneToMany(mappedBy = "classSubject", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Assessment> assessments = new ArrayList<>();

    // Note: StudentAttendance is linked directly to class and subject, not to class_subject
    // @OneToMany(mappedBy = "classSubject", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<StudentAttendance> studentAttendances = new ArrayList<>();

    // Note: TimetableSlot is linked directly to class and subject, not to class_subject
    // @OneToMany(mappedBy = "classSubject", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<TimetableSlot> timetableSlots = new ArrayList<>();

    // Helper methods
    public String getDisplayName() {
        return clazz.getName() + " - " + subject.getName();
    }

    public boolean hasTeacher() {
        return teacher != null;
    }

    public boolean isTaughtBy(Teacher teacher) {
        return this.teacher != null && this.teacher.equals(teacher);
    }
}
