package com.rca.mis.repository;

import com.rca.mis.model.academic.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TermRepository extends JpaRepository<Term, UUID> {

    Optional<Term> findByName(String name);
    
    List<Term> findByAcademicYear_Id(UUID academicYearId);
    
    List<Term> findByIsActiveTrue();
    
    @Query("SELECT t FROM Term t WHERE t.startDate <= :date AND t.endDate >= :date")
    Optional<Term> findByDate(@Param("date") LocalDate date);
    
    @Query("SELECT t FROM Term t WHERE t.academicYear.isCurrent = true")
    List<Term> findByCurrentAcademicYear();
    
    @Query("SELECT t FROM Term t WHERE t.startDate BETWEEN :startDate AND :endDate")
    List<Term> findByStartDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(t) FROM Term t WHERE t.academicYear.id = :academicYearId")
    long countByAcademicYearId(@Param("academicYearId") UUID academicYearId);
    
    boolean existsByNameAndAcademicYear_Id(String name, UUID academicYearId);
}
