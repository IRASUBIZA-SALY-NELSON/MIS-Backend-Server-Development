package com.rca.mis.model.academic;

public enum AcademicYearStatus {
    PLANNING("Planning"),
    ACTIVE("Active"),
    COMPLETED("Completed"),
    ARCHIVED("Archived"),
    CANCELLED("Cancelled");

    private final String displayName;

    AcademicYearStatus(String displayName) {
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
