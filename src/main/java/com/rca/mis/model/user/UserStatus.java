package com.rca.mis.model.user;

/**
 * Enum representing the status of a user account
 */
public enum UserStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    LOCKED("Locked"),
    EXPIRED("Expired"),
    CREDENTIALS_EXPIRED("Credentials Expired"),
    PENDING_ACTIVATION("Pending Activation"),
    PENDING_APPROVAL("Pending Approval");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean canLogin() {
        return this == ACTIVE;
    }

    public boolean requiresAction() {
        return this == PENDING_ACTIVATION || this == PENDING_APPROVAL;
    }
}
