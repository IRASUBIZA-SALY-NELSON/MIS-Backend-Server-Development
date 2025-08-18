package com.rca.mis.model.teacher;

public enum TeacherStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    SUSPENDED("Suspended"),
    ON_LEAVE("On Leave"),
    TERMINATED("Terminated"),
    RETIRED("Retired"),
    PROBATION("Probation"),
    PENDING("Pending"),
    CONTRACT("Contract"),
    PART_TIME("Part Time");

    private final String displayName;

    TeacherStatus(String displayName) {
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
