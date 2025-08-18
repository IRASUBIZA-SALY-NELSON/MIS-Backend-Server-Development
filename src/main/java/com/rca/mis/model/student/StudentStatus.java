package com.rca.mis.model.student;

public enum StudentStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    GRADUATED("Graduated"),
    TRANSFERRED("Transferred"),
    WITHDRAWN("Withdrawn"),
    EXPELLED("Expelled"),
    ON_LEAVE("On Leave"),
    PENDING("Pending"),
    PROBATION("Probation");

    private final String displayName;

    StudentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
