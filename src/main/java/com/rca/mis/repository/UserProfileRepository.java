package com.rca.mis.repository;

import com.rca.mis.model.user.UserProfile;
import com.rca.mis.model.user.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByUser_Id(UUID userId);
    
    Optional<UserProfile> findByUser_Email(String email);
    
    Optional<UserProfile> findByPhone(String phone);
    
    List<UserProfile> findByGender(Gender gender);
    
    @Query("SELECT up FROM UserProfile up WHERE up.firstName LIKE %:name% OR up.lastName LIKE %:name%")
    List<UserProfile> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT up FROM UserProfile up WHERE up.dateOfBirth BETWEEN :startDate AND :endDate")
    List<UserProfile> findByDateOfBirthBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT up FROM UserProfile up WHERE up.address LIKE %:address%")
    List<UserProfile> findByAddressContaining(@Param("address") String address);
    
    boolean existsByPhone(String phone);
    
    @Query("SELECT COUNT(up) FROM UserProfile up WHERE up.gender = :gender")
    long countByGender(@Param("gender") Gender gender);
}
