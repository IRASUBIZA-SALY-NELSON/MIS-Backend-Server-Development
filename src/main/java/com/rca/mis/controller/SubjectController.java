package com.rca.mis.controller;

import com.rca.mis.model.academic.Subject;
import com.rca.mis.model.academic.SubjectCategory;
import com.rca.mis.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@Tag(name = "Subject Management", description = "APIs for managing subjects")
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    @Operation(summary = "Create a new subject", description = "Creates a new subject")
    @ApiResponse(responseCode = "201", description = "Subject created successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<Subject> createSubject(@Valid @RequestBody Subject subject) {
        Subject created = subjectService.createSubject(subject);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update subject", description = "Updates an existing subject")
    @ApiResponse(responseCode = "200", description = "Subject updated successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<Subject> updateSubject(
            @Parameter(description = "Subject ID") @PathVariable UUID id,
            @Valid @RequestBody Subject subject) {
        Subject updated = subjectService.updateSubject(id, subject);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subject by ID", description = "Retrieves a subject by ID")
    @ApiResponse(responseCode = "200", description = "Subject found")
    @ApiResponse(responseCode = "404", description = "Subject not found")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Subject> getSubjectById(@Parameter(description = "Subject ID") @PathVariable UUID id) {
        return subjectService.findById(id)
                .map(subject -> ResponseEntity.ok(subject))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get subject by code", description = "Retrieves a subject by code")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Subject> getSubjectByCode(
            @Parameter(description = "Subject Code") @PathVariable String code) {
        return subjectService.findByCode(code)
                .map(subject -> ResponseEntity.ok(subject))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get subject by name", description = "Retrieves a subject by name")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Subject> getSubjectByName(
            @Parameter(description = "Subject Name") @PathVariable String name) {
        return subjectService.findByName(name)
                .map(subject -> ResponseEntity.ok(subject))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all subjects", description = "Retrieves all subjects")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.findAll();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get subjects by category", description = "Retrieves subjects by category")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Subject>> getSubjectsByCategory(
            @Parameter(description = "Subject Category") @PathVariable SubjectCategory category) {
        List<Subject> subjects = subjectService.findByCategory(category);
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active subjects", description = "Retrieves all active subjects")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Subject>> getActiveSubjects() {
        List<Subject> subjects = subjectService.findActiveSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/credits/{credits}")
    @Operation(summary = "Get subjects by credits", description = "Retrieves subjects by credit value")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Subject>> getSubjectsByCredits(
            @Parameter(description = "Credits") @PathVariable Integer credits) {
        List<Subject> subjects = subjectService.findByCredits(credits);
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/search/name")
    @Operation(summary = "Search subjects by name", description = "Searches subjects by name containing the query")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Subject>> searchSubjectsByName(
            @Parameter(description = "Search query") @RequestParam String name) {
        List<Subject> subjects = subjectService.findByNameContaining(name);
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/search/description")
    @Operation(summary = "Search subjects by description", description = "Searches subjects by description containing the query")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Subject>> searchSubjectsByDescription(
            @Parameter(description = "Search query") @RequestParam String description) {
        List<Subject> subjects = subjectService.findByDescriptionContaining(description);
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/count/category/{category}")
    @Operation(summary = "Count subjects by category", description = "Returns count of subjects by category")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<Long> countSubjectsByCategory(
            @Parameter(description = "Subject Category") @PathVariable SubjectCategory category) {
        long count = subjectService.countByCategory(category);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/total-credits")
    @Operation(summary = "Get total active credits", description = "Returns total credits of all active subjects")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<Integer> getTotalActiveCredits() {
        Integer total = subjectService.getTotalActiveCredits();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/credit-values")
    @Operation(summary = "Get all credit values", description = "Returns list of all credit values")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Integer>> getAllCreditValues() {
        List<Integer> credits = subjectService.getAllCreditValues();
        return ResponseEntity.ok(credits);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete subject", description = "Deletes a subject")
    @ApiResponse(responseCode = "204", description = "Subject deleted successfully")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSubject(@Parameter(description = "Subject ID") @PathVariable UUID id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}
