package com.rca.mis.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

/**
 * Request DTO for user registration
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotNull(message = "Role is required")
    private String role; // STUDENT, TEACHER, ADMIN, PARENT

    // Optional profile fields
    private String phone;
    private String address;
    private LocalDate dateOfBirth;
    private String gender; // MALE, FEMALE, OTHER
    private String nationalId;
    private String emergencyContact;
    private String emergencyPhone;

    // Role-specific fields
    private String studentId; // For STUDENT role
    private String employeeId; // For TEACHER/ADMIN role
    private String department; // For TEACHER/ADMIN role
    private String specialization; // For TEACHER role
    private String parentStudentId; // For PARENT role - ID of student they are parent to
}
