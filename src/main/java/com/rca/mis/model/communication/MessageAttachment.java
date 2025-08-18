package com.rca.mis.model.communication;

import com.rca.mis.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message_attachments")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageAttachment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "original_file_name", nullable = false, length = 255)
    private String originalFileName;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "file_extension", length = 20)
    private String fileExtension;

    @Column(name = "is_downloaded")
    private Boolean isDownloaded = false;

    @Column(name = "download_count")
    private Integer downloadCount = 0;

    @Column(name = "last_downloaded_at")
    private java.time.LocalDateTime lastDownloadedAt;

    @Column(name = "checksum", length = 64)
    private String checksum;

    @Column(name = "description", length = 500)
    private String description;

    // Helper methods
    public String getDisplayName() {
        return originalFileName + " (" + formatFileSize(fileSize) + ")";
    }

    public boolean isDownloaded() {
        return Boolean.TRUE.equals(isDownloaded);
    }

    public boolean isImage() {
        return contentType != null && contentType.startsWith("image/");
    }

    public boolean isDocument() {
        return contentType != null && (contentType.startsWith("application/pdf") ||
               contentType.startsWith("application/msword") ||
               contentType.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
               contentType.startsWith("application/vnd.ms-excel") ||
               contentType.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    }

    public boolean isVideo() {
        return contentType != null && contentType.startsWith("video/");
    }

    public boolean isAudio() {
        return contentType != null && contentType.startsWith("audio/");
    }

    public boolean isArchive() {
        return contentType != null && (contentType.equals("application/zip") ||
               contentType.equals("application/x-rar-compressed") ||
               contentType.equals("application/x-7z-compressed"));
    }

    private String formatFileSize(Long bytes) {
        if (bytes == null) return "0 B";
        
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
