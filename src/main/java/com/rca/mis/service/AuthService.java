package com.rca.mis.service;

import com.rca.mis.dto.request.LoginRequest;
import com.rca.mis.dto.response.AuthResponse;

/**
 * Service interface for authentication operations
 */
public interface AuthService {

    /**
     * Authenticate user and generate JWT tokens
     */
    AuthResponse login(LoginRequest loginRequest);

    /**
     * Refresh access token using refresh token
     */
    AuthResponse refreshToken(String refreshToken);

    /**
     * Logout user and invalidate tokens
     */
    void logout(String accessToken);

    /**
     * Validate access token
     */
    boolean validateAccessToken(String accessToken);

    /**
     * Get user information from token
     */
    String getUsernameFromToken(String token);

    /**
     * Change user password
     */
    void changePassword(String currentPassword, String newPassword, String username);

    /**
     * Request password reset
     */
    void requestPasswordReset(String email);

    /**
     * Reset password using reset token
     */
    void resetPassword(String resetToken, String newPassword);

    /**
     * Verify OTP for password reset
     */
    boolean verifyOTP(String email, String otp);

    /**
     * Register a new user
     */
    AuthResponse register(com.rca.mis.dto.request.RegisterRequest registerRequest);

    /**
     * Get user profile information
     */
    Object getUserProfile(String username);

    /**
     * Update user profile information
     */
    Object updateUserProfile(String username, java.util.Map<String, Object> profileData);
}
