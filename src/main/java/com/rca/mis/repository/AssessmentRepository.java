package com.rca.mis.repository;

import com.rca.mis.model.academic.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {

    List<Assessment> findBySubject_Id(UUID subjectId);
    
    List<Assessment> findByClazz_Id(UUID classId);
    
    List<Assessment> findByTerm_Id(UUID termId);
    
    List<Assessment> findByType(String type);
    
    List<Assessment> findByIsActiveTrue();
    
    @Query("SELECT a FROM Assessment a WHERE a.dueDate BETWEEN :startDate AND :endDate")
    List<Assessment> findByDueDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Assessment a WHERE a.subject.id = :subjectId AND a.term.id = :termId")
    List<Assessment> findBySubjectAndTerm(@Param("subjectId") UUID subjectId, @Param("termId") UUID termId);
    
    @Query("SELECT a FROM Assessment a WHERE a.clazz.id = :classId AND a.term.id = :termId")
    List<Assessment> findByClassAndTerm(@Param("classId") UUID classId, @Param("termId") UUID termId);
    
    @Query("SELECT a FROM Assessment a WHERE a.name LIKE %:name%")
    List<Assessment> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT COUNT(a) FROM Assessment a WHERE a.subject.id = :subjectId")
    long countBySubjectId(@Param("subjectId") UUID subjectId);
    
    @Query("SELECT DISTINCT a.type FROM Assessment a WHERE a.type IS NOT NULL ORDER BY a.type")
    List<String> findAllTypes();
}
