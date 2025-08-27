package com.rca.mis.repository;

import com.rca.mis.model.user.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, UUID> {

    Optional<Guardian> findByUser_Email(String email);
    
    Optional<Guardian> findByUser_Id(UUID userId);
    
    @Query("SELECT g FROM Guardian g WHERE g.user.profile.firstName LIKE %:name% OR g.user.profile.lastName LIKE %:name%")
    List<Guardian> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT g FROM Guardian g WHERE g.relationship LIKE %:relationship%")
    List<Guardian> findByRelationshipContaining(@Param("relationship") String relationship);
    
    @Query("SELECT DISTINCT g.relationship FROM Guardian g WHERE g.relationship IS NOT NULL ORDER BY g.relationship")
    List<String> findAllRelationships();
    
    @Query("SELECT COUNT(g) FROM Guardian g")
    long countAllGuardians();
}
