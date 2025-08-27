package com.rca.mis.service.impl;

import com.rca.mis.dto.request.LoginRequest;
import com.rca.mis.dto.request.RegisterRequest;
import com.rca.mis.dto.response.AuthResponse;
import com.rca.mis.model.user.User;
import com.rca.mis.repository.UserRepository;
import com.rca.mis.service.AuthService;
import com.rca.mis.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Simple authentication service implementation without full Spring Security
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SimpleAuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        log.info("Login attempt for email: {}", loginRequest.getEmail());
        
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        
        log.info("Login successful for user: {}", user.getEmail());
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 hour in seconds
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        log.info("Registration attempt for email: {}", registerRequest.getEmail());
        
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        
        // Create user profile with registration data
        com.rca.mis.model.user.UserProfile profile = new com.rca.mis.model.user.UserProfile();
        profile.setFirstName(registerRequest.getFirstName());
        profile.setLastName(registerRequest.getLastName());
        profile.setPhone(registerRequest.getPhone());
        profile.setAddress(registerRequest.getAddress());
        if (registerRequest.getDateOfBirth() != null) {
            profile.setDateOfBirth(registerRequest.getDateOfBirth());
        }
        if (registerRequest.getGender() != null) {
            profile.setGender(com.rca.mis.model.user.UserProfile.Gender.fromString(registerRequest.getGender()));
        }
        profile.setUser(user);
        user.setProfile(profile);
        
        userRepository.save(user);
        
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        
        log.info("Registration successful for user: {}", user.getEmail());
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 hour in seconds
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtUtil.extractUsername(refreshToken);
        if (email == null || jwtUtil.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        // Need to get user for token generation
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        String newAccessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);
        
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(3600L) // 1 hour in seconds
                .build();
    }

    @Override
    public void logout(String token) {
        // Simple logout - in a real implementation you would blacklist the token
        log.info("User logged out");
    }

    @Override
    public boolean validateAccessToken(String token) {
        try {
            String email = jwtUtil.extractUsername(token);
            return email != null && !jwtUtil.isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        return jwtUtil.extractUsername(token);
    }

    @Override
    public void changePassword(String currentPassword, String newPassword, String username) {
        Optional<User> userOpt = userRepository.findByEmail(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void requestPasswordReset(String email) {
        // Simple implementation - in a real system you would send an email
        log.info("Password reset requested for: {}", email);
    }

    @Override
    public void resetPassword(String resetToken, String newPassword) {
        // Simple implementation - in a real system you would validate the reset token
        throw new RuntimeException("Password reset not implemented in simple mode");
    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        // Simple implementation - always return false for now
        return false;
    }

    @Override
    public Object getUserProfile(String username) {
        log.info("Getting profile for user: {}", username);
        
        Optional<User> userOpt = userRepository.findByEmail(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        var profile = user.getProfile();
        
        // Return basic user profile information
        java.util.Map<String, Object> profileMap = new java.util.HashMap<>();
        profileMap.put("id", user.getId());
        profileMap.put("email", user.getEmail());
        profileMap.put("firstName", profile != null && profile.getFirstName() != null ? profile.getFirstName() : "");
        profileMap.put("lastName", profile != null && profile.getLastName() != null ? profile.getLastName() : "");
        profileMap.put("fullName", profile != null ? profile.getFullName() : user.getEmail());
        profileMap.put("phoneNumber", profile != null && profile.getPhone() != null ? profile.getPhone() : "");
        profileMap.put("address", profile != null && profile.getAddress() != null ? profile.getAddress() : "");
        profileMap.put("dateOfBirth", profile != null && profile.getDateOfBirth() != null ? profile.getDateOfBirth().toString() : "");
        profileMap.put("gender", profile != null && profile.getGender() != null ? profile.getGender().toString() : "");
        profileMap.put("createdAt", user.getCreatedAt() != null ? user.getCreatedAt().toString() : "");
        profileMap.put("updatedAt", user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : "");
        return profileMap;
    }

    @Override
    public Object updateUserProfile(String username, java.util.Map<String, Object> profileData) {
        log.info("Updating profile for user: {}", username);
        
        Optional<User> userOpt = userRepository.findByEmail(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOpt.get();
        var profile = user.getProfile();
        
        // Create profile if it doesn't exist
        if (profile == null) {
            profile = new com.rca.mis.model.user.UserProfile();
            profile.setUser(user);
            user.setProfile(profile);
        }
        
        // Update profile fields if provided
        if (profileData.containsKey("firstName")) {
            profile.setFirstName((String) profileData.get("firstName"));
        }
        if (profileData.containsKey("lastName")) {
            profile.setLastName((String) profileData.get("lastName"));
        }
        if (profileData.containsKey("phoneNumber")) {
            profile.setPhone((String) profileData.get("phoneNumber"));
        }
        if (profileData.containsKey("address")) {
            profile.setAddress((String) profileData.get("address"));
        }
        if (profileData.containsKey("dateOfBirth")) {
            profile.setDateOfBirth(java.time.LocalDate.parse((String) profileData.get("dateOfBirth")));
        }
        if (profileData.containsKey("gender")) {
            String genderStr = (String) profileData.get("gender");
            profile.setGender(com.rca.mis.model.user.UserProfile.Gender.fromString(genderStr));
        }
        
        userRepository.save(user);
        
        log.info("Profile updated successfully for user: {}", username);
        
        // Return updated profile
        return getUserProfile(username);
    }
}
