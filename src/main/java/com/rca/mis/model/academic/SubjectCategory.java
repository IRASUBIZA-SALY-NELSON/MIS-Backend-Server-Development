package com.rca.mis.model.academic;

public enum SubjectCategory {
    CORE("Core"),
    ELECTIVE("Elective"),
    REQUIRED("Required"),
    OPTIONAL("Optional"),
    TECHNICAL("Technical"),
    THEORETICAL("Theoretical"),
    PRACTICAL("Practical"),
    LABORATORY("Laboratory"),
    WORKSHOP("Workshop"),
    PROJECT("Project"),
    INTERNSHIP("Internship"),
    THESIS("Thesis"),
    SEMINAR("Seminar"),
    TUTORIAL("Tutorial"),
    OTHER("Other");

    private final String displayName;

    SubjectCategory(String displayName) {
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
