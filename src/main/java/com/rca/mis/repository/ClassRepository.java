package com.rca.mis.repository;

import com.rca.mis.model.academic.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClassRepository extends JpaRepository<Class, UUID> {

    Optional<Class> findByName(String name);
    
    List<Class> findByAcademicYear_Id(UUID academicYearId);
    
    List<Class> findByIsActiveTrue();
    
    List<Class> findByLevel(String level);
    
    List<Class> findBySection(String section);
    
    @Query("SELECT c FROM Class c WHERE c.academicYear.isCurrent = true")
    List<Class> findByCurrentAcademicYear();
    
    @Query("SELECT c FROM Class c WHERE c.name LIKE %:name%")
    List<Class> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT COUNT(c) FROM Class c WHERE c.academicYear.id = :academicYearId")
    long countByAcademicYearId(@Param("academicYearId") UUID academicYearId);
    
    @Query("SELECT DISTINCT c.level FROM Class c WHERE c.level IS NOT NULL ORDER BY c.level")
    List<String> findAllLevels();
    
    @Query("SELECT DISTINCT c.section FROM Class c WHERE c.section IS NOT NULL ORDER BY c.section")
    List<String> findAllSections();
    
    boolean existsByName(String name);
    
    boolean existsByNameAndAcademicYear_Id(String name, UUID academicYearId);
}
