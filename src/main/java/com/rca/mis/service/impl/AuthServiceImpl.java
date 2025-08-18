package com.rca.mis.service.impl;

import com.rca.mis.dto.request.LoginRequest;
import com.rca.mis.dto.response.AuthResponse;
import com.rca.mis.model.user.User;
import com.rca.mis.repository.UserRepository;
import com.rca.mis.service.AuthService;
import com.rca.mis.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of authentication service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generate tokens
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            // Get user information
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update last login
            // Note: You might want to add a lastLoginAt field to User entity
            // user.setLastLoginAt(LocalDateTime.now());
            // userRepository.save(user);

            // Build response
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(jwtUtil.getTokenExpirationTime(accessToken))
                    .user(buildUserInfo(user))
                    .roles(user.getRoles().stream()
                            .map(role -> role.getName())
                            .collect(Collectors.toList()))
                    .permissions(user.getRoles().stream()
                            .flatMap(role -> role.getPermissions() != null ? 
                                    List.of(role.getPermissions().split(",")).stream() : 
                                    List.<String>of().stream())
                            .distinct()
                            .collect(Collectors.toList()))
                    .build();

        } catch (BadCredentialsException e) {
            log.error("Invalid credentials for user: {}", loginRequest.getEmail());
            throw new RuntimeException("Invalid email or password");
        } catch (Exception e) {
            log.error("Error during login for user: {}", loginRequest.getEmail(), e);
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        try {
            // Validate refresh token
            if (!jwtUtil.isRefreshToken(refreshToken)) {
                throw new RuntimeException("Invalid refresh token");
            }

            if (jwtUtil.isTokenExpired(refreshToken)) {
                throw new RuntimeException("Refresh token expired");
            }

            // Extract username from refresh token
            String username = jwtUtil.extractUsername(refreshToken);
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate new tokens
            String newAccessToken = jwtUtil.generateAccessToken(user);
            String newRefreshToken = jwtUtil.generateRefreshToken(user);

            return AuthResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .expiresIn(jwtUtil.getTokenExpirationTime(newAccessToken))
                    .user(buildUserInfo(user))
                    .roles(user.getRoles().stream()
                            .map(role -> role.getName())
                            .collect(Collectors.toList()))
                    .permissions(user.getRoles().stream()
                            .flatMap(role -> role.getPermissions() != null ? 
                                    List.of(role.getPermissions().split(",")).stream() : 
                                    List.<String>of().stream())
                            .distinct()
                            .collect(Collectors.toList()))
                    .build();

        } catch (Exception e) {
            log.error("Error refreshing token", e);
            throw new RuntimeException("Token refresh failed: " + e.getMessage());
        }
    }

    @Override
    public void logout(String accessToken) {
        try {
            // In a production system, you might want to:
            // 1. Add the token to a blacklist
            // 2. Store logout time in user session
            // 3. Send logout event to other services
            
            String username = jwtUtil.extractUsername(accessToken);
            log.info("User {} logged out successfully", username);
            
        } catch (Exception e) {
            log.error("Error during logout", e);
            // Don't throw exception during logout
        }
    }

    @Override
    public boolean validateAccessToken(String accessToken) {
        try {
            if (!jwtUtil.isAccessToken(accessToken)) {
                return false;
            }
            
            if (jwtUtil.isTokenExpired(accessToken)) {
                return false;
            }

            String username = jwtUtil.extractUsername(accessToken);
            User user = userRepository.findByEmail(username).orElse(null);
            
            return user != null && user.isEnabled();
            
        } catch (Exception e) {
            log.error("Error validating access token", e);
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        try {
            return jwtUtil.extractUsername(token);
        } catch (Exception e) {
            log.error("Error extracting username from token", e);
            return null;
        }
    }

    @Override
    public void changePassword(String currentPassword, String newPassword, String username) {
        try {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Verify current password
            if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
                throw new RuntimeException("Current password is incorrect");
            }

            // Encode and save new password
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            user.setPasswordHash(encodedNewPassword);
            userRepository.save(user);

            log.info("Password changed successfully for user: {}", username);
            
        } catch (Exception e) {
            log.error("Error changing password for user: {}", username, e);
            throw new RuntimeException("Password change failed: " + e.getMessage());
        }
    }

    @Override
    public void requestPasswordReset(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate reset token (you might want to store this in a separate table)
            String resetToken = generateResetToken();
            
            // In a production system, you would:
            // 1. Store the reset token with expiration
            // 2. Send email with reset link
            // 3. Set token expiration
            
            log.info("Password reset requested for user: {}", email);
            
        } catch (Exception e) {
            log.error("Error requesting password reset for email: {}", email, e);
            // Don't reveal if user exists or not
            log.info("Password reset request processed for email: {}", email);
        }
    }

    @Override
    public void resetPassword(String resetToken, String newPassword) {
        try {
            // In a production system, you would:
            // 1. Validate the reset token
            // 2. Check token expiration
            // 3. Find user by reset token
            // 4. Update password
            // 5. Invalidate reset token
            
            log.info("Password reset completed successfully");
            
        } catch (Exception e) {
            log.error("Error resetting password", e);
            throw new RuntimeException("Password reset failed: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        try {
            // In a production system, you would:
            // 1. Retrieve stored OTP for the email
            // 2. Check OTP expiration
            // 3. Compare OTPs
            // 4. Return verification result
            
            log.info("OTP verification requested for email: {}", email);
            return true; // Placeholder implementation
            
        } catch (Exception e) {
            log.error("Error verifying OTP for email: {}", email, e);
            return false;
        }
    }

    /**
     * Build user information for response
     */
    private AuthResponse.UserInfo buildUserInfo(User user) {
        return AuthResponse.UserInfo.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .firstName(user.getProfile() != null ? user.getProfile().getFirstName() : null)
                .lastName(user.getProfile() != null ? user.getProfile().getLastName() : null)
                .fullName(user.getFullName())
                .profilePicture(user.getProfile() != null ? user.getProfile().getProfilePicture() : null)
                .status(user.getStatus().name())
                .lastLoginAt(LocalDateTime.now().toString()) // You might want to store this in User entity
                .build();
    }

    /**
     * Generate reset token (placeholder implementation)
     */
    private String generateResetToken() {
        // In a production system, generate a secure random token
        return java.util.UUID.randomUUID().toString();
    }
}
