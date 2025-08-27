package com.rca.mis.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data initializer to seed essential data when the application starts
 */
@Slf4j
// Temporarily disabled to get server running
// @Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.data.initialization.enabled", havingValue = "true", matchIfMissing = true)
public class DataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");
        
        // Initialize roles if they don't exist
        initializeRoles();
        
        log.info("Data initialization completed successfully");
    }

    private void initializeRoles() {
        log.info("Initializing roles...");
        
        // Check if roles already exist
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM roles", Long.class);
        if (count != null && count > 0) {
            log.info("Roles already exist, skipping initialization");
            return;
        }

        // Create default roles using native SQL to handle JSONB properly
        createRoleIfNotExists("SUPER_ADMIN", "Super Administrator with full system access", "[\"*\"]");
        createRoleIfNotExists("ADMIN", "Administrator with management access", 
            "[\"users:read\", \"users:write\", \"students:read\", \"students:write\", \"teachers:read\", \"teachers:write\", \"classes:read\", \"classes:write\", \"subjects:read\", \"subjects:write\", \"assessments:read\", \"assessments:write\", \"reports:read\", \"reports:write\"]");
        createRoleIfNotExists("TEACHER", "Teacher with class and student management access", 
            "[\"students:read\", \"attendance:read\", \"attendance:write\", \"assessments:read\", \"assessments:write\", \"marks:read\", \"marks:write\", \"timetable:read\"]");
        createRoleIfNotExists("STUDENT", "Student with limited access to their own data", 
            "[\"profile:read\", \"profile:write\", \"marks:read\", \"attendance:read\", \"timetable:read\", \"projects:read\", \"projects:write\"]");
        createRoleIfNotExists("PARENT", "Parent with access to their child's information", 
            "[\"child:read\", \"attendance:read\", \"marks:read\", \"timetable:read\", \"reports:read\"]");
        createRoleIfNotExists("GUARDIAN", "Guardian with access to their ward's information", 
            "[\"ward:read\", \"attendance:read\", \"marks:read\", \"timetable:read\", \"reports:read\"]");

        log.info("Roles initialized successfully");
    }

    private void createRoleIfNotExists(String name, String description, String permissions) {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM roles WHERE name = ?", Long.class, name);
        if (count == null || count == 0) {
            jdbcTemplate.update(
                "INSERT INTO roles (id, name, description, permissions, created_at, updated_at) VALUES (uuid_generate_v4(), ?, ?, ?::jsonb, NOW(), NOW())",
                name, description, permissions
            );
            log.info("Created role: {}", name);
        } else {
            log.info("Role {} already exists", name);
        }
    }
}
