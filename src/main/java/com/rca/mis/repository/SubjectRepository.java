package com.rca.mis.repository;

import com.rca.mis.model.academic.Subject;
import com.rca.mis.model.academic.SubjectCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, UUID> {

    Optional<Subject> findByCode(String code);
    
    Optional<Subject> findByName(String name);
    
    List<Subject> findByCategory(SubjectCategory category);
    
    List<Subject> findByIsActiveTrue();
    
    List<Subject> findByCredits(Integer credits);
    
    @Query("SELECT s FROM Subject s WHERE s.name LIKE %:name%")
    List<Subject> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT s FROM Subject s WHERE s.description LIKE %:description%")
    List<Subject> findByDescriptionContaining(@Param("description") String description);
    
    @Query("SELECT COUNT(s) FROM Subject s WHERE s.category = :category")
    long countByCategory(@Param("category") SubjectCategory category);
    
    @Query("SELECT SUM(s.credits) FROM Subject s WHERE s.isActive = true")
    Integer getTotalActiveCredits();
    
    @Query("SELECT DISTINCT s.credits FROM Subject s ORDER BY s.credits")
    List<Integer> findAllCreditValues();
    
    boolean existsByCode(String code);
    
    boolean existsByName(String name);
}
