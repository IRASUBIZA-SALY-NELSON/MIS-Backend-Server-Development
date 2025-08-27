package com.rca.mis.service;

import com.rca.mis.model.teacher.Teacher;
import com.rca.mis.model.teacher.TeacherStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeacherService {

    Teacher createTeacher(Teacher teacher);
    
    Teacher updateTeacher(UUID id, Teacher teacher);
    
    Optional<Teacher> findById(UUID id);
    
    Optional<Teacher> findByEmployeeCode(String employeeCode);
    
    Optional<Teacher> findByEmail(String email);
    
    List<Teacher> findAll();
    
    Page<Teacher> findAll(Pageable pageable);
    
    List<Teacher> findByStatus(TeacherStatus status);
    
    List<Teacher> findByDepartment(String department);
    
    List<Teacher> findByNameContaining(String name);
    
    List<Teacher> findByQualificationContaining(String qualification);
    
    List<Teacher> findBySpecializationContaining(String specialization);
    
    Teacher changeStatus(UUID id, TeacherStatus status);
    
    Teacher assignToDepartment(UUID teacherId, String department);
    
    void deleteTeacher(UUID id);
    
    boolean existsByEmployeeId(String employeeId);
    
    String generateEmployeeCode();
    
    long countByStatus(TeacherStatus status);
    
    long countByDepartment(String department);
    
    List<String> getAllDepartments();
    
    List<String> getAllQualifications();
    
    List<String> getAllSpecializations();
}
