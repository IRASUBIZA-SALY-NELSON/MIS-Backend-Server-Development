package com.rca.mis.repository;

import com.rca.mis.model.academic.AcademicYear;
import com.rca.mis.model.academic.AcademicYearStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, UUID> {

    Optional<AcademicYear> findByName(String name);
    
    Optional<AcademicYear> findByIsCurrentTrue();
    
    List<AcademicYear> findByStatus(AcademicYearStatus status);
    
    List<AcademicYear> findByIsActiveTrue();
    
    @Query("SELECT ay FROM AcademicYear ay WHERE ay.startDate <= :date AND ay.endDate >= :date")
    Optional<AcademicYear> findByDate(@Param("date") LocalDate date);
    
    @Query("SELECT ay FROM AcademicYear ay WHERE ay.startDate BETWEEN :startDate AND :endDate")
    List<AcademicYear> findByStartDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT ay FROM AcademicYear ay ORDER BY ay.startDate DESC")
    List<AcademicYear> findAllOrderByStartDateDesc();
    
    boolean existsByName(String name);
    
    @Query("SELECT COUNT(ay) FROM AcademicYear ay WHERE ay.isCurrent = true")
    long countCurrentAcademicYears();
}
