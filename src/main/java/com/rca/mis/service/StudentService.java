package com.rca.mis.service;

import com.rca.mis.model.student.Student;
import com.rca.mis.model.student.StudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentService {

    Student createStudent(Student student);
    
    Student updateStudent(UUID id, Student student);
    
    Optional<Student> findById(UUID id);
    
    Optional<Student> findByStudentCode(String studentCode);
    
    Optional<Student> findByEmail(String email);
    
    List<Student> findAll();
    
    Page<Student> findAll(Pageable pageable);
    
    List<Student> findByStatus(StudentStatus status);
    
    List<Student> findByClass(UUID classId);
    
    List<Student> findByNameContaining(String name);
    
    List<Student> findByAdmissionDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Student> findInternationalStudents();
    
    List<Student> findScholarshipStudents();
    
    List<Student> findSpecialNeedsStudents();
    
    List<Student> findBoardingStudents();
    
    Student changeStatus(UUID id, StudentStatus status);
    
    Student assignToClass(UUID studentId, UUID classId);
    
    Student promoteStudent(UUID studentId, UUID newClassId);
    
    void deleteStudent(UUID id);
    
    boolean existsByStudentCode(String studentCode);
    
    boolean existsByEnrollmentNumber(String enrollmentNumber);
    
    String generateStudentCode();
    
    long countByStatus(StudentStatus status);
    
    long countByClass(UUID classId);
    
    double getAttendancePercentage(UUID studentId, LocalDate startDate, LocalDate endDate);
    
    double getAverageMarks(UUID studentId, UUID termId);
}
