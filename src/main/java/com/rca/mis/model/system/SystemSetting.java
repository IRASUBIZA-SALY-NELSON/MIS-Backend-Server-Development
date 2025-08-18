package com.rca.mis.model.system;

import com.rca.mis.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_settings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"setting_key"})
})
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SystemSetting extends BaseEntity {

    @Column(name = "setting_key", nullable = false, length = 100, unique = true)
    private String settingKey;

    @Column(name = "setting_value", columnDefinition = "TEXT")
    private String settingValue;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private SettingCategory category = SettingCategory.GENERAL;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 50)
    private DataType dataType = DataType.STRING;

    @Column(name = "is_encrypted", nullable = false)
    private Boolean isEncrypted = false;

    @Column(name = "is_editable", nullable = false)
    private Boolean isEditable = true;

    @Column(name = "is_visible", nullable = false)
    private Boolean isVisible = true;

    @Column(name = "validation_regex", length = 500)
    private String validationRegex;

    @Column(name = "default_value", columnDefinition = "TEXT")
    private String defaultValue;

    @Column(name = "min_value")
    private String minValue;

    @Column(name = "max_value")
    private String maxValue;

    @Column(name = "allowed_values", columnDefinition = "TEXT")
    private String allowedValues;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @Column(name = "version")
    private Integer version = 1;

    @Column(name = "metadata", columnDefinition = "JSONB")
    private String metadata;

    // Helper methods
    public String getDisplayName() {
        return settingKey + " (" + category.getDisplayName() + ")";
    }

    public boolean isEncrypted() {
        return Boolean.TRUE.equals(isEncrypted);
    }

    public boolean isEditable() {
        return Boolean.TRUE.equals(isEditable);
    }

    public boolean isVisible() {
        return Boolean.TRUE.equals(isVisible);
    }

    public boolean hasValidation() {
        return validationRegex != null && !validationRegex.trim().isEmpty();
    }

    public boolean hasDefaultValue() {
        return defaultValue != null && !defaultValue.trim().isEmpty();
    }

    public boolean hasConstraints() {
        return (minValue != null && !minValue.trim().isEmpty()) ||
               (maxValue != null && !maxValue.trim().isEmpty());
    }

    public boolean hasAllowedValues() {
        return allowedValues != null && !allowedValues.trim().isEmpty();
    }

    public boolean isModified() {
        return lastModifiedAt != null;
    }

    public boolean isStringType() {
        return DataType.STRING.equals(dataType);
    }

    public boolean isNumericType() {
        return DataType.INTEGER.equals(dataType) || DataType.DECIMAL.equals(dataType);
    }

    public boolean isBooleanType() {
        return DataType.BOOLEAN.equals(dataType);
    }

    public boolean isDateType() {
        return DataType.DATE.equals(dataType) || DataType.DATETIME.equals(dataType);
    }

    public enum SettingCategory {
        GENERAL("General"),
        SECURITY("Security"),
        EMAIL("Email"),
        NOTIFICATION("Notification"),
        ACADEMIC("Academic"),
        FINANCIAL("Financial"),
        INTEGRATION("Integration"),
        UI("User Interface"),
        REPORTING("Reporting"),
        MAINTENANCE("Maintenance");

        private final String displayName;

        SettingCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum DataType {
        STRING("String"),
        INTEGER("Integer"),
        DECIMAL("Decimal"),
        BOOLEAN("Boolean"),
        DATE("Date"),
        DATETIME("DateTime"),
        JSON("JSON"),
        XML("XML"),
        BINARY("Binary");

        private final String displayName;

        DataType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
