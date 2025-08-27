package com.rca.mis.controller;

import com.rca.mis.model.teacher.Teacher;
import com.rca.mis.model.teacher.TeacherStatus;
import com.rca.mis.service.TeacherService;
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
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@Tag(name = "Teacher Management", description = "APIs for managing teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    @Operation(summary = "Create a new teacher", description = "Creates a new teacher record")
    @ApiResponse(responseCode = "201", description = "Teacher created successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Teacher> createTeacher(@Valid @RequestBody Teacher teacher) {
        Teacher createdTeacher = teacherService.createTeacher(teacher);
        return new ResponseEntity<>(createdTeacher, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update teacher", description = "Updates an existing teacher record")
    @ApiResponse(responseCode = "200", description = "Teacher updated successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Teacher> updateTeacher(
            @Parameter(description = "Teacher ID") @PathVariable UUID id,
            @Valid @RequestBody Teacher teacher) {
        Teacher updatedTeacher = teacherService.updateTeacher(id, teacher);
        return ResponseEntity.ok(updatedTeacher);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by ID", description = "Retrieves a teacher by their ID")
    @ApiResponse(responseCode = "200", description = "Teacher found")
    @ApiResponse(responseCode = "404", description = "Teacher not found")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('TEACHER')")
    public ResponseEntity<Teacher> getTeacherById(@Parameter(description = "Teacher ID") @PathVariable UUID id) {
        return teacherService.findById(id)
                .map(teacher -> ResponseEntity.ok(teacher))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{employeeCode}")
    @Operation(summary = "Get teacher by employee code", description = "Retrieves a teacher by their employee code")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('TEACHER')")
    public ResponseEntity<Teacher> getTeacherByCode(
            @Parameter(description = "Employee Code") @PathVariable String employeeCode) {
        return teacherService.findByEmployeeCode(employeeCode)
                .map(teacher -> ResponseEntity.ok(teacher))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get teacher by email", description = "Retrieves a teacher by their email")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('TEACHER')")
    public ResponseEntity<Teacher> getTeacherByEmail(
            @Parameter(description = "Teacher Email") @PathVariable String email) {
        return teacherService.findByEmail(email)
                .map(teacher -> ResponseEntity.ok(teacher))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all teachers", description = "Retrieves all teachers with pagination")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('TEACHER')")
    public ResponseEntity<Page<Teacher>> getAllTeachers(Pageable pageable) {
        Page<Teacher> teachers = teacherService.findAll(pageable);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get teachers by status", description = "Retrieves teachers by their status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<Teacher>> getTeachersByStatus(
            @Parameter(description = "Teacher Status") @PathVariable TeacherStatus status) {
        List<Teacher> teachers = teacherService.findByStatus(status);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "Get teachers by department", description = "Retrieves teachers in a specific department")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('TEACHER')")
    public ResponseEntity<List<Teacher>> getTeachersByDepartment(
            @Parameter(description = "Department") @PathVariable String department) {
        List<Teacher> teachers = teacherService.findByDepartment(department);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/search")
    @Operation(summary = "Search teachers by name", description = "Searches teachers by name containing the query")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('TEACHER')")
    public ResponseEntity<List<Teacher>> searchTeachersByName(
            @Parameter(description = "Search query") @RequestParam String name) {
        List<Teacher> teachers = teacherService.findByNameContaining(name);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/qualification")
    @Operation(summary = "Search teachers by qualification", description = "Searches teachers by qualification")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<Teacher>> searchTeachersByQualification(
            @Parameter(description = "Qualification") @RequestParam String qualification) {
        List<Teacher> teachers = teacherService.findByQualificationContaining(qualification);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/specialization")
    @Operation(summary = "Search teachers by specialization", description = "Searches teachers by specialization")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<Teacher>> searchTeachersBySpecialization(
            @Parameter(description = "Specialization") @RequestParam String specialization) {
        List<Teacher> teachers = teacherService.findBySpecializationContaining(specialization);
        return ResponseEntity.ok(teachers);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change teacher status", description = "Changes the status of a teacher")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Teacher> changeTeacherStatus(
            @Parameter(description = "Teacher ID") @PathVariable UUID id,
            @Parameter(description = "New status") @RequestParam TeacherStatus status) {
        Teacher updatedTeacher = teacherService.changeStatus(id, status);
        return ResponseEntity.ok(updatedTeacher);
    }

    @PatchMapping("/{teacherId}/assign-department")
    @Operation(summary = "Assign teacher to department", description = "Assigns a teacher to a specific department")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Teacher> assignTeacherToDepartment(
            @Parameter(description = "Teacher ID") @PathVariable UUID teacherId,
            @Parameter(description = "Department") @RequestParam String department) {
        Teacher updatedTeacher = teacherService.assignToDepartment(teacherId, department);
        return ResponseEntity.ok(updatedTeacher);
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Count teachers by status", description = "Returns count of teachers by status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Long> countTeachersByStatus(
            @Parameter(description = "Teacher Status") @PathVariable TeacherStatus status) {
        long count = teacherService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/department/{department}")
    @Operation(summary = "Count teachers by department", description = "Returns count of teachers in a department")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Long> countTeachersByDepartment(
            @Parameter(description = "Department") @PathVariable String department) {
        long count = teacherService.countByDepartment(department);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/departments")
    @Operation(summary = "Get all departments", description = "Returns list of all departments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('TEACHER')")
    public ResponseEntity<List<String>> getAllDepartments() {
        List<String> departments = teacherService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/qualifications")
    @Operation(summary = "Get all qualifications", description = "Returns list of all qualifications")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<String>> getAllQualifications() {
        List<String> qualifications = teacherService.getAllQualifications();
        return ResponseEntity.ok(qualifications);
    }

    @GetMapping("/specializations")
    @Operation(summary = "Get all specializations", description = "Returns list of all specializations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<String>> getAllSpecializations() {
        List<String> specializations = teacherService.getAllSpecializations();
        return ResponseEntity.ok(specializations);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete teacher", description = "Deletes a teacher record")
    @ApiResponse(responseCode = "204", description = "Teacher deleted successfully")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTeacher(@Parameter(description = "Teacher ID") @PathVariable UUID id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
