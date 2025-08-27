package com.rca.mis.repository;

import com.rca.mis.model.user.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParentRepository extends JpaRepository<Parent, UUID> {

    Optional<Parent> findByUser_Email(String email);
    
    Optional<Parent> findByUser_Id(UUID userId);
    
    @Query("SELECT p FROM Parent p WHERE p.user.profile.firstName LIKE %:name% OR p.user.profile.lastName LIKE %:name%")
    List<Parent> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT p FROM Parent p WHERE p.occupation LIKE %:occupation%")
    List<Parent> findByOccupationContaining(@Param("occupation") String occupation);
    
    @Query("SELECT p FROM Parent p WHERE p.employer LIKE %:employer%")
    List<Parent> findByEmployerContaining(@Param("employer") String employer);
    
    @Query("SELECT DISTINCT p.occupation FROM Parent p WHERE p.occupation IS NOT NULL ORDER BY p.occupation")
    List<String> findAllOccupations();
    
    @Query("SELECT COUNT(p) FROM Parent p")
    long countAllParents();
}
