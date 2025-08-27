package com.rca.mis.service;

import com.rca.mis.model.academic.Subject;
import com.rca.mis.model.academic.SubjectCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubjectService {

    Subject createSubject(Subject subject);
    
    Subject updateSubject(UUID id, Subject subject);
    
    Optional<Subject> findById(UUID id);
    
    Optional<Subject> findByCode(String code);
    
    Optional<Subject> findByName(String name);
    
    List<Subject> findAll();
    
    List<Subject> findByCategory(SubjectCategory category);
    
    List<Subject> findActiveSubjects();
    
    List<Subject> findByCredits(Integer credits);
    
    List<Subject> findByNameContaining(String name);
    
    List<Subject> findByDescriptionContaining(String description);
    
    void deleteSubject(UUID id);
    
    boolean existsByCode(String code);
    
    boolean existsByName(String name);
    
    long countByCategory(SubjectCategory category);
    
    Integer getTotalActiveCredits();
    
    List<Integer> getAllCreditValues();
}
