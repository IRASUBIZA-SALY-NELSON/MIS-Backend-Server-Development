package com.rca.mis.repository;

import com.rca.mis.model.student.Student;
import com.rca.mis.model.student.StudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findByStudentCode(String studentCode);
    
    Optional<Student> findByEnrollmentNumber(String enrollmentNumber);
    
    Optional<Student> findByRollNumber(String rollNumber);
    
    Optional<Student> findByUser_Email(String email);
    
    List<Student> findByStatus(StudentStatus status);
    
    List<Student> findByCurrentClass_Id(UUID classId);
    
    List<Student> findByParent_Id(UUID parentId);
    
    List<Student> findByGuardian_Id(UUID guardianId);
    
    Page<Student> findByStatusAndCurrentClass_Id(StudentStatus status, UUID classId, Pageable pageable);
    
    @Query("SELECT s FROM Student s WHERE s.user.profile.firstName LIKE %:name% OR s.user.profile.lastName LIKE %:name%")
    List<Student> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT s FROM Student s WHERE s.status = :status AND s.currentClass.academicYear.id = :academicYearId")
    List<Student> findByStatusAndAcademicYear(@Param("status") StudentStatus status, @Param("academicYearId") UUID academicYearId);
    
    @Query("SELECT s FROM Student s WHERE s.admissionDate BETWEEN :startDate AND :endDate")
    List<Student> findByAdmissionDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT s FROM Student s WHERE s.isInternational = true")
    List<Student> findInternationalStudents();
    
    @Query("SELECT s FROM Student s WHERE s.isScholarship = true")
    List<Student> findScholarshipStudents();
    
    @Query("SELECT s FROM Student s WHERE s.isSpecialNeeds = true")
    List<Student> findSpecialNeedsStudents();
    
    @Query("SELECT s FROM Student s WHERE s.isBoarding = true")
    List<Student> findBoardingStudents();
    
    @Query("SELECT s FROM Student s WHERE s.visaExpiryDate BETWEEN :startDate AND :endDate")
    List<Student> findByVisaExpiryDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.status = :status")
    long countByStatus(@Param("status") StudentStatus status);
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.currentClass.id = :classId")
    long countByClassId(@Param("classId") UUID classId);
    
    @Query("SELECT COUNT(s) FROM Student s WHERE s.currentClass.academicYear.id = :academicYearId")
    long countByAcademicYearId(@Param("academicYearId") UUID academicYearId);
    
    boolean existsByStudentCode(String studentCode);
    
    boolean existsByEnrollmentNumber(String enrollmentNumber);
    
    boolean existsByRollNumber(String rollNumber);
}
