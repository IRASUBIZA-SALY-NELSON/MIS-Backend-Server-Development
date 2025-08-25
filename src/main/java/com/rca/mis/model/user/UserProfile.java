package com.rca.mis.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.rca.mis.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * User profile entity containing detailed user information
 */
@Data
@Entity
@Table(name = "user_profiles")
@EqualsAndHashCode(callSuper = true, exclude = {"user"})
public class UserProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(length = 20)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "profile_picture", length = 500)
    private String profilePicture;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    /**
     * Get user's full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Get user's age based on date of birth
     */
    public Integer getAge() {
        if (dateOfBirth == null) {
            return null;
        }
        
        LocalDate now = LocalDate.now();
        int age = now.getYear() - dateOfBirth.getYear();
        
        if (now.getMonthValue() < dateOfBirth.getMonthValue() || 
            (now.getMonthValue() == dateOfBirth.getMonthValue() && now.getDayOfMonth() < dateOfBirth.getDayOfMonth())) {
            age--;
        }
        
        return age;
    }

    /**
     * Check if user is a minor (under 18)
     */
    public boolean isMinor() {
        Integer age = getAge();
        return age != null && age < 18;
    }

    /**
     * Get initials for display purposes
     */
    public String getInitials() {
        if (firstName == null || lastName == null) {
            return "";
        }
        
        String firstInitial = firstName.length() > 0 ? firstName.substring(0, 1).toUpperCase() : "";
        String lastInitial = lastName.length() > 0 ? lastName.substring(0, 1).toUpperCase() : "";
        
        return firstInitial + lastInitial;
    }

    /**
     * Gender enum for user profiles
     */
    public enum Gender {
        MALE("Male"),
        FEMALE("Female"),
        OTHER("Other"),
        PREFER_NOT_TO_SAY("Prefer not to say");

        private final String displayName;

        Gender(String displayName) {
            this.displayName = displayName;
        }

        @JsonValue
        public String getDisplayName() {
            return displayName;
        }

        /**
         * Case-insensitive valueOf method for JSON deserialization
         */
        @JsonCreator
        public static Gender fromString(String value) {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            
            String upperValue = value.trim().toUpperCase();
            switch (upperValue) {
                case "MALE":
                    return MALE;
                case "FEMALE":
                    return FEMALE;
                case "OTHER":
                    return OTHER;
                case "PREFER_NOT_TO_SAY":
                    return PREFER_NOT_TO_SAY;
                default:
                    throw new IllegalArgumentException("Invalid gender value: " + value);
            }
        }
    }
}
