package com.rca.mis.repository;

import com.rca.mis.model.teacher.Teacher;
import com.rca.mis.model.teacher.TeacherStatus;
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
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

    Optional<Teacher> findByEmployeeId(String employeeId);
    
    // Temporarily commented out to fix server startup
    // Optional<Teacher> findByUser_Email(String email);
    
    List<Teacher> findByStatus(TeacherStatus status);
    
    List<Teacher> findByDepartment(String department);
    
    // @Query("SELECT t FROM Teacher t WHERE t.user.profile.firstName LIKE %:name% OR t.user.profile.lastName LIKE %:name%")
    // List<Teacher> findByNameContaining(@Param("name") String name);
    
    // @Query("SELECT t FROM Teacher t WHERE t.hireDate BETWEEN :startDate AND :endDate")
    // List<Teacher> findByJoiningDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // @Query("SELECT t FROM Teacher t WHERE t.qualification LIKE %:qualification%")
    // List<Teacher> findByQualificationContaining(@Param("qualification") String qualification);
    
    // @Query("SELECT t FROM Teacher t WHERE t.specialization LIKE %:specialization%")
    // List<Teacher> findBySpecializationContaining(@Param("specialization") String specialization);
    
    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.status = :status")
    long countByStatus(@Param("status") TeacherStatus status);
    
    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.department = :department")
    long countByDepartment(@Param("department") String department);
    
    // @Query("SELECT DISTINCT t.department FROM Teacher t WHERE t.department IS NOT NULL ORDER BY t.department")
    // List<String> findAllDepartments();
    
    // @Query("SELECT DISTINCT t.qualification FROM Teacher t WHERE t.qualification IS NOT NULL ORDER BY t.qualification")
    // List<String> findAllQualifications();
    
    // @Query("SELECT DISTINCT t.specialization FROM Teacher t WHERE t.specialization IS NOT NULL ORDER BY t.specialization")
    // List<String> findAllSpecializations();
    
    boolean existsByEmployeeId(String employeeId);
    
    Page<Teacher> findByStatusAndDepartment(TeacherStatus status, String department, Pageable pageable);
}
