package com.rca.mis.service;

import com.rca.mis.model.academic.AcademicYear;
import com.rca.mis.model.academic.AcademicYearStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AcademicYearService {

    AcademicYear createAcademicYear(AcademicYear academicYear);
    
    AcademicYear updateAcademicYear(UUID id, AcademicYear academicYear);
    
    Optional<AcademicYear> findById(UUID id);
    
    Optional<AcademicYear> findByName(String name);
    
    Optional<AcademicYear> findCurrentAcademicYear();
    
    List<AcademicYear> findAll();
    
    List<AcademicYear> findByStatus(AcademicYearStatus status);
    
    List<AcademicYear> findActiveAcademicYears();
    
    AcademicYear setCurrentAcademicYear(UUID id);
    
    AcademicYear changeStatus(UUID id, AcademicYearStatus status);
    
    void deleteAcademicYear(UUID id);
    
    boolean existsByName(String name);
    
    Optional<AcademicYear> findByDate(LocalDate date);
}
