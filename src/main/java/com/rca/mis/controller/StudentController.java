package com.rca.mis.controller;

import com.rca.mis.model.student.Student;
import com.rca.mis.model.student.StudentStatus;
import com.rca.mis.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Student Management", description = "APIs for managing students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Create a new student", description = "Creates a new student record")
    @ApiResponse(responseCode = "201", description = "Student created successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        Student createdStudent = studentService.createStudent(student);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student", description = "Updates an existing student record")
    @ApiResponse(responseCode = "200", description = "Student updated successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<Student> updateStudent(
            @Parameter(description = "Student ID") @PathVariable UUID id,
            @Valid @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(id, student);
        return ResponseEntity.ok(updatedStudent);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Retrieves a student by their ID")
    @ApiResponse(responseCode = "200", description = "Student found")
    @ApiResponse(responseCode = "404", description = "Student not found")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Student> getStudentById(@Parameter(description = "Student ID") @PathVariable UUID id) {
        return studentService.findById(id)
                .map(student -> ResponseEntity.ok(student))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{studentCode}")
    @Operation(summary = "Get student by code", description = "Retrieves a student by their student code")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Student> getStudentByCode(
            @Parameter(description = "Student Code") @PathVariable String studentCode) {
        return studentService.findByStudentCode(studentCode)
                .map(student -> ResponseEntity.ok(student))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get student by email", description = "Retrieves a student by their email")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Student> getStudentByEmail(
            @Parameter(description = "Student Email") @PathVariable String email) {
        return studentService.findByEmail(email)
                .map(student -> ResponseEntity.ok(student))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all students", description = "Retrieves all students with pagination")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Page<Student>> getAllStudents(Pageable pageable) {
        Page<Student> students = studentService.findAll(pageable);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get students by status", description = "Retrieves students by their status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Student>> getStudentsByStatus(
            @Parameter(description = "Student Status") @PathVariable StudentStatus status) {
        List<Student> students = studentService.findByStatus(status);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/class/{classId}")
    @Operation(summary = "Get students by class", description = "Retrieves students in a specific class")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Student>> getStudentsByClass(
            @Parameter(description = "Class ID") @PathVariable UUID classId) {
        List<Student> students = studentService.findByClass(classId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/search")
    @Operation(summary = "Search students by name", description = "Searches students by name containing the query")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Student>> searchStudentsByName(
            @Parameter(description = "Search query") @RequestParam String name) {
        List<Student> students = studentService.findByNameContaining(name);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/admission-date")
    @Operation(summary = "Get students by admission date range", description = "Retrieves students admitted within a date range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Student>> getStudentsByAdmissionDate(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Student> students = studentService.findByAdmissionDateBetween(startDate, endDate);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/international")
    @Operation(summary = "Get international students", description = "Retrieves all international students")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Student>> getInternationalStudents() {
        List<Student> students = studentService.findInternationalStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/scholarship")
    @Operation(summary = "Get scholarship students", description = "Retrieves all scholarship students")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Student>> getScholarshipStudents() {
        List<Student> students = studentService.findScholarshipStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/special-needs")
    @Operation(summary = "Get special needs students", description = "Retrieves all special needs students")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Student>> getSpecialNeedsStudents() {
        List<Student> students = studentService.findSpecialNeedsStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/boarding")
    @Operation(summary = "Get boarding students", description = "Retrieves all boarding students")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<List<Student>> getBoardingStudents() {
        List<Student> students = studentService.findBoardingStudents();
        return ResponseEntity.ok(students);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change student status", description = "Changes the status of a student")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<Student> changeStudentStatus(
            @Parameter(description = "Student ID") @PathVariable UUID id,
            @Parameter(description = "New status") @RequestParam StudentStatus status) {
        Student updatedStudent = studentService.changeStatus(id, status);
        return ResponseEntity.ok(updatedStudent);
    }

    @PatchMapping("/{studentId}/assign-class/{classId}")
    @Operation(summary = "Assign student to class", description = "Assigns a student to a specific class")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<Student> assignStudentToClass(
            @Parameter(description = "Student ID") @PathVariable UUID studentId,
            @Parameter(description = "Class ID") @PathVariable UUID classId) {
        Student updatedStudent = studentService.assignToClass(studentId, classId);
        return ResponseEntity.ok(updatedStudent);
    }

    @PatchMapping("/{studentId}/promote/{newClassId}")
    @Operation(summary = "Promote student", description = "Promotes a student to a new class")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<Student> promoteStudent(
            @Parameter(description = "Student ID") @PathVariable UUID studentId,
            @Parameter(description = "New Class ID") @PathVariable UUID newClassId) {
        Student promotedStudent = studentService.promoteStudent(studentId, newClassId);
        return ResponseEntity.ok(promotedStudent);
    }

    @GetMapping("/{studentId}/attendance-percentage")
    @Operation(summary = "Get student attendance percentage", description = "Calculates attendance percentage for a student")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Double> getStudentAttendancePercentage(
            @Parameter(description = "Student ID") @PathVariable UUID studentId,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        double percentage = studentService.getAttendancePercentage(studentId, startDate, endDate);
        return ResponseEntity.ok(percentage);
    }

    @GetMapping("/{studentId}/average-marks/{termId}")
    @Operation(summary = "Get student average marks", description = "Calculates average marks for a student in a term")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Double> getStudentAverageMarks(
            @Parameter(description = "Student ID") @PathVariable UUID studentId,
            @Parameter(description = "Term ID") @PathVariable UUID termId) {
        double average = studentService.getAverageMarks(studentId, termId);
        return ResponseEntity.ok(average);
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Count students by status", description = "Returns count of students by status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('REGISTRAR')")
    public ResponseEntity<Long> countStudentsByStatus(
            @Parameter(description = "Student Status") @PathVariable StudentStatus status) {
        long count = studentService.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/class/{classId}")
    @Operation(summary = "Count students by class", description = "Returns count of students in a class")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Long> countStudentsByClass(
            @Parameter(description = "Class ID") @PathVariable UUID classId) {
        long count = studentService.countByClass(classId);
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student", description = "Deletes a student record")
    @ApiResponse(responseCode = "204", description = "Student deleted successfully")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@Parameter(description = "Student ID") @PathVariable UUID id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
