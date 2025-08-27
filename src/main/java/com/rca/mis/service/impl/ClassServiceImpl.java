package com.rca.mis.service.impl;

import com.rca.mis.model.academic.Class;
import com.rca.mis.repository.ClassRepository;
import com.rca.mis.service.ClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;

    @Override
    public Class createClass(Class classEntity) {
        log.info("Creating new class: {}", classEntity.getName());
        
        if (existsByName(classEntity.getName())) {
            throw new IllegalArgumentException("Class already exists: " + classEntity.getName());
        }
        
        return classRepository.save(classEntity);
    }

    @Override
    public Class updateClass(UUID id, Class classEntity) {
        log.info("Updating class with ID: {}", id);
        
        Class existingClass = classRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Class not found with ID: " + id));
        
        existingClass.setName(classEntity.getName());
        existingClass.setDescription(classEntity.getDescription());
        existingClass.setLevel(classEntity.getLevel());
        existingClass.setSection(classEntity.getSection());
        existingClass.setCapacity(classEntity.getCapacity());
        existingClass.setAcademicYear(classEntity.getAcademicYear());
        existingClass.setIsActive(classEntity.getIsActive());
        
        return classRepository.save(existingClass);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Class> findById(UUID id) {
        return classRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Class> findByName(String name) {
        return classRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Class> findAll() {
        return classRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Class> findByAcademicYear(UUID academicYearId) {
        return classRepository.findByAcademicYear_Id(academicYearId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Class> findActiveClasses() {
        return classRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Class> findByLevel(String level) {
        return classRepository.findByLevel(level);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Class> findBySection(String section) {
        return classRepository.findBySection(section);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Class> findByCurrentAcademicYear() {
        return classRepository.findByCurrentAcademicYear();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Class> findByNameContaining(String name) {
        return classRepository.findByNameContaining(name);
    }

    @Override
    public void deleteClass(UUID id) {
        log.info("Deleting class with ID: {}", id);
        
        if (!classRepository.existsById(id)) {
            throw new IllegalArgumentException("Class not found with ID: " + id);
        }
        
        classRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return classRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndAcademicYear(String name, UUID academicYearId) {
        return classRepository.existsByNameAndAcademicYear_Id(name, academicYearId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByAcademicYear(UUID academicYearId) {
        return classRepository.countByAcademicYearId(academicYearId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllLevels() {
        return classRepository.findAllLevels();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllSections() {
        return classRepository.findAllSections();
    }
}
