package com.rca.mis.controller;

import com.rca.mis.model.academic.Class;
import com.rca.mis.service.ClassService;
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
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@Tag(name = "Class Management", description = "APIs for managing classes")
public class ClassController {

    private final ClassService classService;

    @PostMapping
    @Operation(summary = "Create a new class", description = "Creates a new class")
    @ApiResponse(responseCode = "201", description = "Class created successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<Class> createClass(@Valid @RequestBody Class classEntity) {
        Class created = classService.createClass(classEntity);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update class", description = "Updates an existing class")
    @ApiResponse(responseCode = "200", description = "Class updated successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<Class> updateClass(
            @Parameter(description = "Class ID") @PathVariable UUID id,
            @Valid @RequestBody Class classEntity) {
        Class updated = classService.updateClass(id, classEntity);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get class by ID", description = "Retrieves a class by ID")
    @ApiResponse(responseCode = "200", description = "Class found")
    @ApiResponse(responseCode = "404", description = "Class not found")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Class> getClassById(@Parameter(description = "Class ID") @PathVariable UUID id) {
        return classService.findById(id)
                .map(classEntity -> ResponseEntity.ok(classEntity))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get class by name", description = "Retrieves a class by name")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Class> getClassByName(
            @Parameter(description = "Class Name") @PathVariable String name) {
        return classService.findByName(name)
                .map(classEntity -> ResponseEntity.ok(classEntity))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all classes", description = "Retrieves all classes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Class>> getAllClasses() {
        List<Class> classes = classService.findAll();
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/academic-year/{academicYearId}")
    @Operation(summary = "Get classes by academic year", description = "Retrieves classes by academic year")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Class>> getClassesByAcademicYear(
            @Parameter(description = "Academic Year ID") @PathVariable UUID academicYearId) {
        List<Class> classes = classService.findByAcademicYear(academicYearId);
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active classes", description = "Retrieves all active classes")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Class>> getActiveClasses() {
        List<Class> classes = classService.findActiveClasses();
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/current-academic-year")
    @Operation(summary = "Get classes for current academic year", description = "Retrieves classes for current academic year")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Class>> getClassesForCurrentAcademicYear() {
        List<Class> classes = classService.findByCurrentAcademicYear();
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/level/{level}")
    @Operation(summary = "Get classes by level", description = "Retrieves classes by level")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Class>> getClassesByLevel(
            @Parameter(description = "Level") @PathVariable String level) {
        List<Class> classes = classService.findByLevel(level);
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/section/{section}")
    @Operation(summary = "Get classes by section", description = "Retrieves classes by section")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Class>> getClassesBySection(
            @Parameter(description = "Section") @PathVariable String section) {
        List<Class> classes = classService.findBySection(section);
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/search")
    @Operation(summary = "Search classes by name", description = "Searches classes by name containing the query")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Class>> searchClassesByName(
            @Parameter(description = "Search query") @RequestParam String name) {
        List<Class> classes = classService.findByNameContaining(name);
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/count/academic-year/{academicYearId}")
    @Operation(summary = "Count classes by academic year", description = "Returns count of classes in academic year")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<Long> countClassesByAcademicYear(
            @Parameter(description = "Academic Year ID") @PathVariable UUID academicYearId) {
        long count = classService.countByAcademicYear(academicYearId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/levels")
    @Operation(summary = "Get all levels", description = "Returns list of all class levels")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<String>> getAllLevels() {
        List<String> levels = classService.getAllLevels();
        return ResponseEntity.ok(levels);
    }

    @GetMapping("/sections")
    @Operation(summary = "Get all sections", description = "Returns list of all class sections")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<String>> getAllSections() {
        List<String> sections = classService.getAllSections();
        return ResponseEntity.ok(sections);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete class", description = "Deletes a class")
    @ApiResponse(responseCode = "204", description = "Class deleted successfully")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClass(@Parameter(description = "Class ID") @PathVariable UUID id) {
        classService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }
}
