package com.rca.mis.security;

import com.rca.mis.model.user.User;
import com.rca.mis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom user details service for Spring Security
 */
@Slf4j
// Temporarily disabled - requires full Spring Security
// @Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            log.debug("Loading user details for email: {}", email);
            
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            // Check if user is active
            if (!user.isEnabled()) {
                log.warn("User account is disabled: {}", email);
                throw new UsernameNotFoundException("User account is disabled: " + email);
            }

            log.debug("User details loaded successfully for email: {}", email);
            return user;

        } catch (UsernameNotFoundException e) {
            log.warn("User not found: {}", email);
            throw e;
        } catch (Exception e) {
            log.error("Error loading user details for email: {}", email, e);
            throw new UsernameNotFoundException("Error loading user details for email: " + email, e);
        }
    }
}
