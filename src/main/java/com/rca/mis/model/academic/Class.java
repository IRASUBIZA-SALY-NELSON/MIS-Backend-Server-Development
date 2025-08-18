package com.rca.mis.model.academic;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.student.Student;
import com.rca.mis.model.student.StudentAttendance;
import com.rca.mis.model.teacher.Teacher;
import com.rca.mis.model.teacher.TeacherClass;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classes")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Class extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "level", nullable = false, length = 50)
    private String level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    private AcademicYear academicYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_teacher_id")
    private Teacher classTeacher;

    @Column(name = "max_students", nullable = false)
    private Integer maxStudents;

    @Column(name = "current_students", nullable = false)
    private Integer currentStudents = 0;

    @Column(name = "room_number", length = 20)
    private String roomNumber;

    @Column(name = "building", length = 50)
    private String building;

    @Column(name = "floor", length = 10)
    private String floor;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "schedule", columnDefinition = "TEXT")
    private String schedule;

    @Column(name = "academic_level", length = 50)
    private String academicLevel;

    @Column(name = "stream", length = 50)
    private String stream;

    @Column(name = "section", length = 10)
    private String section;

    @Column(name = "semester", length = 20)
    private String semester;

    @Column(name = "academic_program", length = 100)
    private String academicProgram;

    @Column(name = "curriculum_version", length = 20)
    private String curriculumVersion;

    @Column(name = "start_date")
    private java.time.LocalDate startDate;

    @Column(name = "end_date")
    private java.time.LocalDate endDate;

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline = false;

    @Column(name = "online_platform", length = 100)
    private String onlinePlatform;

    @Column(name = "online_link")
    private String onlineLink;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "currentClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassSubject> classSubjects = new ArrayList<>();

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeacherClass> teacherClasses = new ArrayList<>();

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assessment> assessments = new ArrayList<>();

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentAttendance> studentAttendances = new ArrayList<>();

    @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimetableSlot> timetableSlots = new ArrayList<>();

    // Helper methods
    public String getDisplayName() {
        return name + " - " + level + " (" + academicYear.getName() + ")";
    }

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }

    public boolean isOnline() {
        return Boolean.TRUE.equals(isOnline);
    }

    public boolean hasClassTeacher() {
        return classTeacher != null;
    }

    public boolean hasRoom() {
        return roomNumber != null && !roomNumber.trim().isEmpty();
    }

    public boolean hasBuilding() {
        return building != null && !building.trim().isEmpty();
    }

    public boolean hasFloor() {
        return floor != null && !floor.trim().isEmpty();
    }

    public boolean hasSchedule() {
        return schedule != null && !schedule.trim().isEmpty();
    }

    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }

    public boolean hasAcademicLevel() {
        return academicLevel != null && !academicLevel.trim().isEmpty();
    }

    public boolean hasStream() {
        return stream != null && !stream.trim().isEmpty();
    }

    public boolean hasSection() {
        return section != null && !section.trim().isEmpty();
    }

    public boolean hasSemester() {
        return semester != null && !semester.trim().isEmpty();
    }

    public boolean hasAcademicProgram() {
        return academicProgram != null && !academicProgram.trim().isEmpty();
    }

    public boolean hasCurriculumVersion() {
        return curriculumVersion != null && !curriculumVersion.trim().isEmpty();
    }

    public boolean hasStartDate() {
        return startDate != null;
    }

    public boolean hasEndDate() {
        return endDate != null;
    }

    public boolean hasOnlineDetails() {
        return isOnline() && onlinePlatform != null && onlineLink != null;
    }

    public boolean hasStudents() {
        return students != null && !students.isEmpty();
    }

    public boolean hasSubjects() {
        return classSubjects != null && !classSubjects.isEmpty();
    }

    public boolean hasTeachers() {
        return teacherClasses != null && !teacherClasses.isEmpty();
    }

    public boolean hasAssessments() {
        return assessments != null && !assessments.isEmpty();
    }

    public boolean hasAttendanceRecords() {
        return studentAttendances != null && !studentAttendances.isEmpty();
    }

    public boolean hasTimetable() {
        return timetableSlots != null && !timetableSlots.isEmpty();
    }

    public boolean isFull() {
        return currentStudents != null && maxStudents != null && currentStudents >= maxStudents;
    }

    public boolean hasAvailableSeats() {
        return currentStudents != null && maxStudents != null && currentStudents < maxStudents;
    }

    public Integer getAvailableSeats() {
        if (currentStudents != null && maxStudents != null) {
            return Math.max(0, maxStudents - currentStudents);
        }
        return null;
    }

    public boolean isOvercrowded() {
        return currentStudents != null && capacity != null && currentStudents > capacity;
    }

    public boolean isUnderEnrolled() {
        return currentStudents != null && maxStudents != null && 
               currentStudents < (maxStudents * 0.5); // Less than 50% capacity
    }

    public boolean isOptimalEnrollment() {
        return currentStudents != null && maxStudents != null && 
               currentStudents >= (maxStudents * 0.7) && currentStudents <= (maxStudents * 0.9);
    }

    public String getEnrollmentStatus() {
        if (isFull()) return "Full";
        if (isOvercrowded()) return "Overcrowded";
        if (isUnderEnrolled()) return "Under Enrolled";
        if (isOptimalEnrollment()) return "Optimal";
        return "Normal";
    }

    public boolean isCurrentlyActive() {
        if (!hasStartDate()) return true;
        
        java.time.LocalDate now = java.time.LocalDate.now();
        return now.isAfter(startDate.minusDays(1)) &&
               (endDate == null || now.isBefore(endDate.plusDays(1)));
    }
}
