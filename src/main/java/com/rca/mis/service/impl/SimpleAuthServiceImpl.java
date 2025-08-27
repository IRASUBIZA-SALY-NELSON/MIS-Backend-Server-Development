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
}
