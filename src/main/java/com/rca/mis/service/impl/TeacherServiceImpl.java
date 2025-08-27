package com.rca.mis.service.impl;

import com.rca.mis.model.teacher.Teacher;
import com.rca.mis.model.teacher.TeacherStatus;
import com.rca.mis.repository.TeacherRepository;
import com.rca.mis.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    @Override
    public Teacher createTeacher(Teacher teacher) {
        log.info("Creating new teacher with email: {}", teacher.getUser().getEmail());
        
        if (teacher.getEmployeeId() == null || teacher.getEmployeeId().isEmpty()) {
            teacher.setEmployeeId(generateEmployeeCode());
        }
        
        if (existsByEmployeeId(teacher.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists: " + teacher.getEmployeeId());
        }
        
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher updateTeacher(UUID id, Teacher teacher) {
        log.info("Updating teacher with ID: {}", id);
        
        Teacher existingTeacher = teacherRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + id));
        
        existingTeacher.setDepartment(teacher.getDepartment());
        existingTeacher.setStatus(teacher.getStatus());
        existingTeacher.setQualification(teacher.getQualification());
        existingTeacher.setSpecialization(teacher.getSpecialization());
        existingTeacher.setExperienceYears(teacher.getExperienceYears());
        existingTeacher.setHireDate(teacher.getHireDate());
        existingTeacher.setNotes(teacher.getNotes());
        
        return teacherRepository.save(existingTeacher);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Teacher> findById(UUID id) {
        return teacherRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Teacher> findByEmployeeCode(String employeeCode) {
        return teacherRepository.findByEmployeeId(employeeCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Teacher> findByEmail(String email) {
        return teacherRepository.findByUser_Email(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Teacher> findAll(Pageable pageable) {
        return teacherRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findByStatus(TeacherStatus status) {
        return teacherRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findByDepartment(String department) {
        return teacherRepository.findByDepartment(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findByNameContaining(String name) {
        return teacherRepository.findByNameContaining(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findByQualificationContaining(String qualification) {
        return teacherRepository.findByQualificationContaining(qualification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Teacher> findBySpecializationContaining(String specialization) {
        return teacherRepository.findBySpecializationContaining(specialization);
    }

    @Override
    public Teacher changeStatus(UUID id, TeacherStatus status) {
        log.info("Changing status for teacher ID: {} to {}", id, status);
        
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + id));
        
        teacher.setStatus(status);
        
        if (status == TeacherStatus.TERMINATED) {
            // Handle termination logic if needed
        }
        
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher assignToDepartment(UUID teacherId, String department) {
        log.info("Assigning teacher ID: {} to department: {}", teacherId, department);
        
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + teacherId));
        
        teacher.setDepartment(department);
        return teacherRepository.save(teacher);
    }

    @Override
    public void deleteTeacher(UUID id) {
        log.info("Deleting teacher with ID: {}", id);
        
        if (!teacherRepository.existsById(id)) {
            throw new IllegalArgumentException("Teacher not found with ID: " + id);
        }
        
        teacherRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmployeeId(String employeeId) {
        return teacherRepository.existsByEmployeeId(employeeId);
    }

    @Override
    public String generateEmployeeCode() {
        String prefix = "TCH" + Year.now().getValue();
        long count = teacherRepository.count() + 1;
        return prefix + String.format("%04d", count);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(TeacherStatus status) {
        return teacherRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByDepartment(String department) {
        return teacherRepository.countByDepartment(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllDepartments() {
        return teacherRepository.findAllDepartments();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllQualifications() {
        return teacherRepository.findAllQualifications();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllSpecializations() {
        return teacherRepository.findAllSpecializations();
    }
}
