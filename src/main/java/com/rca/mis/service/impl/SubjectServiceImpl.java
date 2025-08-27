package com.rca.mis.service.impl;

import com.rca.mis.model.academic.Subject;
import com.rca.mis.model.academic.SubjectCategory;
import com.rca.mis.repository.SubjectRepository;
import com.rca.mis.service.SubjectService;
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
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    @Override
    public Subject createSubject(Subject subject) {
        log.info("Creating new subject: {}", subject.getName());
        
        if (existsByCode(subject.getCode())) {
            throw new IllegalArgumentException("Subject code already exists: " + subject.getCode());
        }
        
        if (existsByName(subject.getName())) {
            throw new IllegalArgumentException("Subject name already exists: " + subject.getName());
        }
        
        return subjectRepository.save(subject);
    }

    @Override
    public Subject updateSubject(UUID id, Subject subject) {
        log.info("Updating subject with ID: {}", id);
        
        Subject existingSubject = subjectRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Subject not found with ID: " + id));
        
        existingSubject.setName(subject.getName());
        existingSubject.setCode(subject.getCode());
        existingSubject.setDescription(subject.getDescription());
        existingSubject.setCategory(subject.getCategory());
        existingSubject.setCredits(subject.getCredits());
        existingSubject.setIsActive(subject.getIsActive());
        
        return subjectRepository.save(existingSubject);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Subject> findById(UUID id) {
        return subjectRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Subject> findByCode(String code) {
        return subjectRepository.findByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Subject> findByName(String name) {
        return subjectRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> findByCategory(SubjectCategory category) {
        return subjectRepository.findByCategory(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> findActiveSubjects() {
        return subjectRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> findByCredits(Integer credits) {
        return subjectRepository.findByCredits(credits);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> findByNameContaining(String name) {
        return subjectRepository.findByNameContaining(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> findByDescriptionContaining(String description) {
        return subjectRepository.findByDescriptionContaining(description);
    }

    @Override
    public void deleteSubject(UUID id) {
        log.info("Deleting subject with ID: {}", id);
        
        if (!subjectRepository.existsById(id)) {
            throw new IllegalArgumentException("Subject not found with ID: " + id);
        }
        
        subjectRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return subjectRepository.existsByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return subjectRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCategory(SubjectCategory category) {
        return subjectRepository.countByCategory(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalActiveCredits() {
        Integer total = subjectRepository.getTotalActiveCredits();
        return total != null ? total : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getAllCreditValues() {
        return subjectRepository.findAllCreditValues();
    }
}
