package com.rca.mis.controller;

import com.rca.mis.dto.request.LoginRequest;
import com.rca.mis.dto.response.AuthResponse;
import com.rca.mis.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication operations
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    /**
     * User login endpoint
     */
    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user and return JWT tokens")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            log.info("Login attempt for user: {}", loginRequest.getEmail());
            
            AuthResponse response = authService.login(loginRequest);
            
            log.info("Login successful for user: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Login failed for user: {}", loginRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .accessToken(null)
                            .refreshToken(null)
                            .build());
        }
    }

    /**
     * Refresh token endpoint
     */
    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh Token", description = "Refresh access token using refresh token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken) {
        try {
            log.info("Token refresh requested");
            
            AuthResponse response = authService.refreshToken(refreshToken);
            
            log.info("Token refresh successful");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Token refresh failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .accessToken(null)
                            .refreshToken(null)
                            .build());
        }
    }

    /**
     * User logout endpoint
     */
    @PostMapping("/logout")
    @Operation(summary = "User Logout", description = "Logout user and invalidate tokens")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            
            if (token != null) {
                authService.logout(token);
                log.info("User logged out successfully");
            }
            
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            log.error("Logout failed", e);
            // Return success even if logout fails to avoid client issues
            return ResponseEntity.ok().build();
        }
    }

    /**
     * Validate token endpoint
     */
    @PostMapping("/validate-token")
    @Operation(summary = "Validate Token", description = "Validate access token")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            
            if (token == null) {
                return ResponseEntity.ok(false);
            }
            
            boolean isValid = authService.validateAccessToken(token);
            return ResponseEntity.ok(isValid);
            
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return ResponseEntity.ok(false);
        }
    }

    /**
     * Change password endpoint
     */
    @PostMapping("/change-password")
    @Operation(summary = "Change Password", description = "Change user password")
    public ResponseEntity<Void> changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            String username = authService.getUsernameFromToken(token);
            
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            authService.changePassword(currentPassword, newPassword, username);
            
            log.info("Password changed successfully for user: {}", username);
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            log.error("Password change failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Request password reset endpoint
     */
    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot Password", description = "Request password reset")
    public ResponseEntity<Void> forgotPassword(@RequestParam String email) {
        try {
            log.info("Password reset requested for email: {}", email);
            
            authService.requestPasswordReset(email);
            
            // Always return success to prevent email enumeration
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            log.error("Password reset request failed for email: {}", email, e);
            // Always return success to prevent email enumeration
            return ResponseEntity.ok().build();
        }
    }

    /**
     * Reset password endpoint
     */
    @PostMapping("/reset-password")
    @Operation(summary = "Reset Password", description = "Reset password using reset token")
    public ResponseEntity<Void> resetPassword(
            @RequestParam String resetToken,
            @RequestParam String newPassword) {
        try {
            log.info("Password reset attempted");
            
            authService.resetPassword(resetToken, newPassword);
            
            log.info("Password reset successful");
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            log.error("Password reset failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Verify OTP endpoint
     */
    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP", description = "Verify OTP for password reset")
    public ResponseEntity<Boolean> verifyOTP(
            @RequestParam String email,
            @RequestParam String otp) {
        try {
            log.info("OTP verification requested for email: {}", email);
            
            boolean isValid = authService.verifyOTP(email, otp);
            
            log.info("OTP verification result: {}", isValid);
            return ResponseEntity.ok(isValid);
            
        } catch (Exception e) {
            log.error("OTP verification failed for email: {}", email, e);
            return ResponseEntity.ok(false);
        }
    }

    /**
     * Health check endpoint for authentication service
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Check authentication service health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Authentication service is running");
    }

    /**
     * Extract token from Authorization header
     */
    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring(7);
    }
}
