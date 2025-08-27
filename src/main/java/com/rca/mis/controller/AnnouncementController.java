package com.rca.mis.controller;

import com.rca.mis.model.communication.Announcement;
import com.rca.mis.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
@Tag(name = "Announcement Management", description = "APIs for managing announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping
    @Operation(summary = "Create announcement", description = "Creates a new announcement")
    @ApiResponse(responseCode = "201", description = "Announcement created successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Announcement> createAnnouncement(@Valid @RequestBody Announcement announcement) {
        Announcement created = announcementService.createAnnouncement(announcement);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update announcement", description = "Updates an existing announcement")
    @ApiResponse(responseCode = "200", description = "Announcement updated successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<Announcement> updateAnnouncement(
            @Parameter(description = "Announcement ID") @PathVariable UUID id,
            @Valid @RequestBody Announcement announcement) {
        Announcement updated = announcementService.updateAnnouncement(id, announcement);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get announcement by ID", description = "Retrieves an announcement by ID")
    @ApiResponse(responseCode = "200", description = "Announcement found")
    @ApiResponse(responseCode = "404", description = "Announcement not found")
    public ResponseEntity<Announcement> getAnnouncementById(@Parameter(description = "Announcement ID") @PathVariable UUID id) {
        return announcementService.findById(id)
                .map(announcement -> ResponseEntity.ok(announcement))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all announcements", description = "Retrieves all announcements with pagination")
    public ResponseEntity<Page<Announcement>> getAllAnnouncements(Pageable pageable) {
        Page<Announcement> announcements = announcementService.findAll(pageable);
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active announcements", description = "Retrieves all currently active announcements")
    public ResponseEntity<List<Announcement>> getActiveAnnouncements() {
        List<Announcement> announcements = announcementService.findActiveAnnouncements();
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/audience/{audience}")
    @Operation(summary = "Get announcements by audience", description = "Retrieves announcements for specific audience")
    public ResponseEntity<List<Announcement>> getAnnouncementsByAudience(
            @Parameter(description = "Target Audience") @PathVariable String audience) {
        List<Announcement> announcements = announcementService.findByTargetAudience(audience);
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "Get announcements by priority", description = "Retrieves announcements by priority level")
    public ResponseEntity<List<Announcement>> getAnnouncementsByPriority(
            @Parameter(description = "Priority") @PathVariable String priority) {
        List<Announcement> announcements = announcementService.findByPriority(priority);
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get announcements by author", description = "Retrieves announcements created by specific author")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<List<Announcement>> getAnnouncementsByAuthor(
            @Parameter(description = "Author ID") @PathVariable UUID authorId) {
        List<Announcement> announcements = announcementService.findByAuthor(authorId);
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/search/title")
    @Operation(summary = "Search announcements by title", description = "Searches announcements by title")
    public ResponseEntity<List<Announcement>> searchAnnouncementsByTitle(
            @Parameter(description = "Title search query") @RequestParam String title) {
        List<Announcement> announcements = announcementService.findByTitleContaining(title);
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/search/content")
    @Operation(summary = "Search announcements by content", description = "Searches announcements by content")
    public ResponseEntity<List<Announcement>> searchAnnouncementsByContent(
            @Parameter(description = "Content search query") @RequestParam String content) {
        List<Announcement> announcements = announcementService.findByContentContaining(content);
        return ResponseEntity.ok(announcements);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete announcement", description = "Deletes an announcement")
    @ApiResponse(responseCode = "204", description = "Announcement deleted successfully")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAnnouncement(@Parameter(description = "Announcement ID") @PathVariable UUID id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }
}
