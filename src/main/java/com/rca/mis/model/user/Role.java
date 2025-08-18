package com.rca.mis.model.user;

import com.rca.mis.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

/**
 * Role entity representing user roles and permissions
 */
@Data
@Entity
@Table(name = "roles")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "JSONB")
    private String permissions; // JSON string of permissions

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    /**
     * Check if role has a specific permission
     */
    public boolean hasPermission(String permission) {
        if (permissions == null || permissions.isEmpty()) {
            return false;
        }
        
        // Simple permission check - in production, use proper JSON parsing
        return permissions.contains(permission) || permissions.contains("*");
    }

    /**
     * Check if role has any of the specified permissions
     */
    public boolean hasAnyPermission(String... permissions) {
        for (String permission : permissions) {
            if (hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if role has all of the specified permissions
     */
    public boolean hasAllPermissions(String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get role name for Spring Security
     */
    public String getAuthority() {
        return "ROLE_" + name;
    }
}
