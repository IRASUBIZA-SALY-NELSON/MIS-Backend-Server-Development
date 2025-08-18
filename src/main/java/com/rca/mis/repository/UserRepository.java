package com.rca.mis.repository;

import com.rca.mis.model.user.User;
import com.rca.mis.model.user.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by email and status
     */
    Optional<User> findByEmailAndStatus(String email, UserStatus status);

    /**
     * Check if user exists by email
     */
    boolean existsByEmail(String email);

    /**
     * Find users by status
     */
    List<User> findByStatus(UserStatus status);

    /**
     * Find users by status with pagination
     */
    Page<User> findByStatus(UserStatus status, Pageable pageable);

    /**
     * Find users by role name
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);

    /**
     * Find users by role name with pagination
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    Page<User> findByRoleName(@Param("roleName") String roleName, Pageable pageable);

    /**
     * Find users by multiple role names
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r.name IN :roleNames")
    List<User> findByRoleNames(@Param("roleNames") List<String> roleNames);

    /**
     * Find users by email containing (for search functionality)
     */
    List<User> findByEmailContainingIgnoreCase(String email);

    /**
     * Find users by email containing with pagination
     */
    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    /**
     * Find active users
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    List<User> findActiveUsers();

    /**
     * Find active users with pagination
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    Page<User> findActiveUsers(Pageable pageable);

    /**
     * Count users by status
     */
    long countByStatus(UserStatus status);

    /**
     * Count users by role name
     */
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = :roleName")
    long countByRoleName(@Param("roleName") String roleName);

    /**
     * Find users created after a specific date
     */
    List<User> findByCreatedAtAfter(java.time.LocalDateTime date);

    /**
     * Find users created between two dates
     */
    List<User> findByCreatedAtBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);

    /**
     * Find users by profile first name or last name containing
     */
    @Query("SELECT u FROM User u JOIN u.profile p WHERE p.firstName LIKE %:name% OR p.lastName LIKE %:name%")
    List<User> findByProfileNameContaining(@Param("name") String name);

    /**
     * Find users by profile first name or last name containing with pagination
     */
    @Query("SELECT u FROM User u JOIN u.profile p WHERE p.firstName LIKE %:name% OR p.lastName LIKE %:name%")
    Page<User> findByProfileNameContaining(@Param("name") String name, Pageable pageable);

    /**
     * Find users by profile phone number
     */
    @Query("SELECT u FROM User u JOIN u.profile p WHERE p.phone = :phone")
    Optional<User> findByProfilePhone(@Param("phone") String phone);

    /**
     * Find users by profile gender
     */
    @Query("SELECT u FROM User u JOIN u.profile p WHERE p.gender = :gender")
    List<User> findByProfileGender(@Param("gender") String gender);

    /**
     * Find users by profile gender with pagination
     */
    @Query("SELECT u FROM User u JOIN u.profile p WHERE p.gender = :gender")
    Page<User> findByProfileGender(@Param("gender") String gender, Pageable pageable);

    /**
     * Find users with profile information (eager loading)
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.profile LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<User> findByIdWithProfileAndRoles(@Param("id") UUID id);

    /**
     * Find all users with profile information (eager loading)
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.profile LEFT JOIN FETCH u.roles")
    List<User> findAllWithProfileAndRoles();

    /**
     * Find all users with profile information and pagination (eager loading)
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.profile LEFT JOIN FETCH u.roles")
    Page<User> findAllWithProfileAndRoles(Pageable pageable);
}
