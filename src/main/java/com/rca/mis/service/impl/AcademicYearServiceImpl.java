package com.rca.mis.service.impl;

import com.rca.mis.model.academic.AcademicYear;
import com.rca.mis.model.academic.AcademicYearStatus;
import com.rca.mis.repository.AcademicYearRepository;
import com.rca.mis.service.AcademicYearService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AcademicYearServiceImpl implements AcademicYearService {

    private final AcademicYearRepository academicYearRepository;

    @Override
    public AcademicYear createAcademicYear(AcademicYear academicYear) {
        log.info("Creating new academic year: {}", academicYear.getName());
        
        if (existsByName(academicYear.getName())) {
            throw new IllegalArgumentException("Academic year already exists: " + academicYear.getName());
        }
        
        return academicYearRepository.save(academicYear);
    }

    @Override
    public AcademicYear updateAcademicYear(UUID id, AcademicYear academicYear) {
        log.info("Updating academic year with ID: {}", id);
        
        AcademicYear existingAcademicYear = academicYearRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Academic year not found with ID: " + id));
        
        existingAcademicYear.setName(academicYear.getName());
        existingAcademicYear.setStartDate(academicYear.getStartDate());
        existingAcademicYear.setEndDate(academicYear.getEndDate());
        existingAcademicYear.setStatus(academicYear.getStatus());
        existingAcademicYear.setIsActive(academicYear.getIsActive());
        existingAcademicYear.setDescription(academicYear.getDescription());
        
        return academicYearRepository.save(existingAcademicYear);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcademicYear> findById(UUID id) {
        return academicYearRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcademicYear> findByName(String name) {
        return academicYearRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcademicYear> findCurrentAcademicYear() {
        return academicYearRepository.findByIsCurrentTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicYear> findAll() {
        return academicYearRepository.findAllOrderByStartDateDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicYear> findByStatus(AcademicYearStatus status) {
        return academicYearRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AcademicYear> findActiveAcademicYears() {
        return academicYearRepository.findByIsActiveTrue();
    }

    @Override
    public AcademicYear setCurrentAcademicYear(UUID id) {
        log.info("Setting academic year as current with ID: {}", id);
        
        // First, unset all current academic years
        List<AcademicYear> currentAcademicYears = academicYearRepository.findByIsActiveTrue();
        currentAcademicYears.forEach(ay -> ay.setIsCurrent(false));
        academicYearRepository.saveAll(currentAcademicYears);
        
        // Set the new current academic year
        AcademicYear academicYear = academicYearRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Academic year not found with ID: " + id));
        
        academicYear.setIsCurrent(true);
        academicYear.setIsActive(true);
        
        return academicYearRepository.save(academicYear);
    }

    @Override
    public AcademicYear changeStatus(UUID id, AcademicYearStatus status) {
        log.info("Changing status for academic year ID: {} to {}", id, status);
        
        AcademicYear academicYear = academicYearRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Academic year not found with ID: " + id));
        
        academicYear.setStatus(status);
        
        if (status == AcademicYearStatus.ARCHIVED) {
            academicYear.setIsActive(false);
            academicYear.setIsCurrent(false);
        }
        
        return academicYearRepository.save(academicYear);
    }

    @Override
    public void deleteAcademicYear(UUID id) {
        log.info("Deleting academic year with ID: {}", id);
        
        if (!academicYearRepository.existsById(id)) {
            throw new IllegalArgumentException("Academic year not found with ID: " + id);
        }
        
        academicYearRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return academicYearRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AcademicYear> findByDate(LocalDate date) {
        return academicYearRepository.findByDate(date);
    }
}
