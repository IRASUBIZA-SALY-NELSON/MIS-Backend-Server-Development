package com.rca.mis.service;

import com.rca.mis.model.academic.Class;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassService {

    Class createClass(Class classEntity);
    
    Class updateClass(UUID id, Class classEntity);
    
    Optional<Class> findById(UUID id);
    
    Optional<Class> findByName(String name);
    
    List<Class> findAll();
    
    List<Class> findByAcademicYear(UUID academicYearId);
    
    List<Class> findActiveClasses();
    
    List<Class> findByLevel(String level);
    
    List<Class> findBySection(String section);
    
    List<Class> findByCurrentAcademicYear();
    
    List<Class> findByNameContaining(String name);
    
    void deleteClass(UUID id);
    
    boolean existsByName(String name);
    
    boolean existsByNameAndAcademicYear(String name, UUID academicYearId);
    
    long countByAcademicYear(UUID academicYearId);
    
    List<String> getAllLevels();
    
    List<String> getAllSections();
}
