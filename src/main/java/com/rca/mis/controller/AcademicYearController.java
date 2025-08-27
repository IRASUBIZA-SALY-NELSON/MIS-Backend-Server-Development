package com.rca.mis.controller;

import com.rca.mis.model.academic.AcademicYear;
import com.rca.mis.model.academic.AcademicYearStatus;
import com.rca.mis.service.AcademicYearService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/academic-years")
@RequiredArgsConstructor
@Tag(name = "Academic Year Management", description = "APIs for managing academic years")
public class AcademicYearController {

    private final AcademicYearService academicYearService;

    @PostMapping
    @Operation(summary = "Create a new academic year", description = "Creates a new academic year")
    @ApiResponse(responseCode = "201", description = "Academic year created successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<AcademicYear> createAcademicYear(@Valid @RequestBody AcademicYear academicYear) {
        AcademicYear created = academicYearService.createAcademicYear(academicYear);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update academic year", description = "Updates an existing academic year")
    @ApiResponse(responseCode = "200", description = "Academic year updated successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<AcademicYear> updateAcademicYear(
            @Parameter(description = "Academic Year ID") @PathVariable UUID id,
            @Valid @RequestBody AcademicYear academicYear) {
        AcademicYear updated = academicYearService.updateAcademicYear(id, academicYear);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get academic year by ID", description = "Retrieves an academic year by ID")
    @ApiResponse(responseCode = "200", description = "Academic year found")
    @ApiResponse(responseCode = "404", description = "Academic year not found")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<AcademicYear> getAcademicYearById(@Parameter(description = "Academic Year ID") @PathVariable UUID id) {
        return academicYearService.findById(id)
                .map(academicYear -> ResponseEntity.ok(academicYear))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get academic year by name", description = "Retrieves an academic year by name")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<AcademicYear> getAcademicYearByName(
            @Parameter(description = "Academic Year Name") @PathVariable String name) {
        return academicYearService.findByName(name)
                .map(academicYear -> ResponseEntity.ok(academicYear))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/current")
    @Operation(summary = "Get current academic year", description = "Retrieves the current academic year")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR') or hasRole('STUDENT')")
    public ResponseEntity<AcademicYear> getCurrentAcademicYear() {
        return academicYearService.findCurrentAcademicYear()
                .map(academicYear -> ResponseEntity.ok(academicYear))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all academic years", description = "Retrieves all academic years")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<AcademicYear>> getAllAcademicYears() {
        List<AcademicYear> academicYears = academicYearService.findAll();
        return ResponseEntity.ok(academicYears);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get academic years by status", description = "Retrieves academic years by status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<List<AcademicYear>> getAcademicYearsByStatus(
            @Parameter(description = "Academic Year Status") @PathVariable AcademicYearStatus status) {
        List<AcademicYear> academicYears = academicYearService.findByStatus(status);
        return ResponseEntity.ok(academicYears);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active academic years", description = "Retrieves all active academic years")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<AcademicYear>> getActiveAcademicYears() {
        List<AcademicYear> academicYears = academicYearService.findActiveAcademicYears();
        return ResponseEntity.ok(academicYears);
    }

    @GetMapping("/by-date")
    @Operation(summary = "Get academic year by date", description = "Retrieves academic year containing the specified date")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<AcademicYear> getAcademicYearByDate(
            @Parameter(description = "Date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return academicYearService.findByDate(date)
                .map(academicYear -> ResponseEntity.ok(academicYear))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/set-current")
    @Operation(summary = "Set current academic year", description = "Sets an academic year as current")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<AcademicYear> setCurrentAcademicYear(@Parameter(description = "Academic Year ID") @PathVariable UUID id) {
        AcademicYear updated = academicYearService.setCurrentAcademicYear(id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change academic year status", description = "Changes the status of an academic year")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<AcademicYear> changeAcademicYearStatus(
            @Parameter(description = "Academic Year ID") @PathVariable UUID id,
            @Parameter(description = "New status") @RequestParam AcademicYearStatus status) {
        AcademicYear updated = academicYearService.changeStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete academic year", description = "Deletes an academic year")
    @ApiResponse(responseCode = "204", description = "Academic year deleted successfully")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAcademicYear(@Parameter(description = "Academic Year ID") @PathVariable UUID id) {
        academicYearService.deleteAcademicYear(id);
        return ResponseEntity.noContent().build();
    }
}
