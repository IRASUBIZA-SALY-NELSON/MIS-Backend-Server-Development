package com.rca.mis.controller;

import com.rca.mis.model.user.User;
import com.rca.mis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/fix-passwords")
    public ResponseEntity<String> fixPasswords() {
        try {
            // Update admin password
            User admin = userRepository.findByEmail("admin@rca.ac.rw").orElse(null);
            if (admin != null) {
                admin.setPasswordHash(passwordEncoder.encode("Admin@123"));
                userRepository.save(admin);
                log.info("Updated admin password");
            }

            // Update teacher password
            User teacher = userRepository.findByEmail("teacher@rca.ac.rw").orElse(null);
            if (teacher != null) {
                teacher.setPasswordHash(passwordEncoder.encode("Teacher@123"));
                userRepository.save(teacher);
                log.info("Updated teacher password");
            }

            // Update student password
            User student = userRepository.findByEmail("student@rca.ac.rw").orElse(null);
            if (student != null) {
                student.setPasswordHash(passwordEncoder.encode("Student@123"));
                userRepository.save(student);
                log.info("Updated student password");
            }

            return ResponseEntity.ok("Password hashes updated successfully");
        } catch (Exception e) {
            log.error("Error updating password hashes", e);
            return ResponseEntity.internalServerError().body("Error updating passwords: " + e.getMessage());
        }
    }
}
