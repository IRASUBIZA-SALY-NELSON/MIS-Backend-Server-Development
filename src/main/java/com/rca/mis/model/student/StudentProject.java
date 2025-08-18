package com.rca.mis.model.student;

import com.rca.mis.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student_projects")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentProject extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "github_link")
    private String githubLink;

    @Column(name = "website_link")
    private String websiteLink;

    @ElementCollection
    @CollectionTable(name = "student_project_tags", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ProjectStatus status = ProjectStatus.IN_PROGRESS;

    @Column(name = "submitted_date")
    private LocalDate submittedDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "grade")
    private Double grade;

    @Column(name = "max_grade")
    private Double maxGrade;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "technologies_used", columnDefinition = "TEXT")
    private String technologiesUsed;

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "featured_date")
    private LocalDate featuredDate;

    @Column(name = "views_count")
    private Integer viewsCount = 0;

    @Column(name = "likes_count")
    private Integer likesCount = 0;

    // Helper methods
    public String getDisplayName() {
        return student.getUser().getProfile().getFullName() + " - " + title;
    }

    public boolean isSubmitted() {
        return submittedDate != null;
    }

    public boolean isOverdue() {
        return dueDate != null && LocalDate.now().isAfter(dueDate) && !isSubmitted();
    }

    public boolean isCompleted() {
        return ProjectStatus.COMPLETED.equals(status);
    }

    public boolean isFeatured() {
        return Boolean.TRUE.equals(isFeatured);
    }

    public boolean hasGrade() {
        return grade != null;
    }

    public Double calculatePercentage() {
        if (grade != null && maxGrade != null && maxGrade > 0) {
            return (grade / maxGrade) * 100;
        }
        return null;
    }

    public boolean hasLinks() {
        return (githubLink != null && !githubLink.trim().isEmpty()) ||
               (websiteLink != null && !websiteLink.trim().isEmpty());
    }

    public enum ProjectStatus {
        IN_PROGRESS("In Progress"),
        SUBMITTED("Submitted"),
        UNDER_REVIEW("Under Review"),
        COMPLETED("Completed"),
        REJECTED("Rejected"),
        ON_HOLD("On Hold");

        private final String displayName;

        ProjectStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
