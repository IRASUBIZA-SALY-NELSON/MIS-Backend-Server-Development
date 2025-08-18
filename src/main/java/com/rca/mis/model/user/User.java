package com.rca.mis.model.user;

import com.rca.mis.model.BaseEntity;
import com.rca.mis.model.communication.Message;
import com.rca.mis.model.communication.Notification;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User entity representing system users
 */
@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile profile;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Message> sentMessages = new HashSet<>();

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Message> receivedMessages = new HashSet<>();

    // Spring Security UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status != UserStatus.EXPIRED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status != UserStatus.CREDENTIALS_EXPIRED;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }

    /**
     * Check if user has a specific role
     */
    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    /**
     * Check if user has any of the specified roles
     */
    public boolean hasAnyRole(String... roleNames) {
        return roles.stream()
                .anyMatch(role -> {
                    for (String roleName : roleNames) {
                        if (role.getName().equals(roleName)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    /**
     * Get user's full name from profile
     */
    public String getFullName() {
        if (profile != null) {
            return profile.getFirstName() + " " + profile.getLastName();
        }
        return email;
    }

    /**
     * Get user's display name (first name if available, otherwise email)
     */
    public String getDisplayName() {
        if (profile != null && profile.getFirstName() != null) {
            return profile.getFirstName();
        }
        return email;
    }
}
