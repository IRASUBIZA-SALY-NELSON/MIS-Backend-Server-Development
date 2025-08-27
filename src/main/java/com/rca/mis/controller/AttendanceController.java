package com.rca.mis.controller;

import com.rca.mis.model.student.StudentAttendance;
import com.rca.mis.service.AttendanceService;
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
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance Management", description = "APIs for managing student attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @Operation(summary = "Mark attendance", description = "Marks attendance for a student")
    @ApiResponse(responseCode = "201", description = "Attendance marked successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<StudentAttendance> markAttendance(@Valid @RequestBody StudentAttendance attendance) {
        StudentAttendance marked = attendanceService.markAttendance(attendance);
        return new ResponseEntity<>(marked, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update attendance", description = "Updates existing attendance record")
    @ApiResponse(responseCode = "200", description = "Attendance updated successfully")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<StudentAttendance> updateAttendance(
            @Parameter(description = "Attendance ID") @PathVariable UUID id,
            @Valid @RequestBody StudentAttendance attendance) {
        StudentAttendance updated = attendanceService.updateAttendance(id, attendance);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get attendance by ID", description = "Retrieves attendance record by ID")
    @ApiResponse(responseCode = "200", description = "Attendance found")
    @ApiResponse(responseCode = "404", description = "Attendance not found")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<StudentAttendance> getAttendanceById(@Parameter(description = "Attendance ID") @PathVariable UUID id) {
        return attendanceService.findById(id)
                .map(attendance -> ResponseEntity.ok(attendance))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get attendance by student", description = "Retrieves all attendance records for a student")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<StudentAttendance>> getAttendanceByStudent(
            @Parameter(description = "Student ID") @PathVariable UUID studentId) {
        List<StudentAttendance> attendances = attendanceService.findByStudent(studentId);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/class/{classId}")
    @Operation(summary = "Get attendance by class", description = "Retrieves all attendance records for a class")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<StudentAttendance>> getAttendanceByClass(
            @Parameter(description = "Class ID") @PathVariable UUID classId) {
        List<StudentAttendance> attendances = attendanceService.findByClass(classId);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/subject/{subjectId}")
    @Operation(summary = "Get attendance by subject", description = "Retrieves all attendance records for a subject")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<StudentAttendance>> getAttendanceBySubject(
            @Parameter(description = "Subject ID") @PathVariable UUID subjectId) {
        List<StudentAttendance> attendances = attendanceService.findBySubject(subjectId);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "Get attendance by date", description = "Retrieves all attendance records for a specific date")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<StudentAttendance>> getAttendanceByDate(
            @Parameter(description = "Date") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<StudentAttendance> attendances = attendanceService.findByDate(date);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get attendance by date range", description = "Retrieves attendance records within a date range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<StudentAttendance>> getAttendanceByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<StudentAttendance> attendances = attendanceService.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/student/{studentId}/date-range")
    @Operation(summary = "Get student attendance by date range", description = "Retrieves attendance records for a student within a date range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<List<StudentAttendance>> getStudentAttendanceByDateRange(
            @Parameter(description = "Student ID") @PathVariable UUID studentId,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<StudentAttendance> attendances = attendanceService.findByStudentAndDateRange(studentId, startDate, endDate);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/student/{studentId}/percentage")
    @Operation(summary = "Get student attendance percentage", description = "Calculates attendance percentage for a student")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Double> getStudentAttendancePercentage(
            @Parameter(description = "Student ID") @PathVariable UUID studentId,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        double percentage = attendanceService.getAttendancePercentageByStudent(studentId, startDate, endDate);
        return ResponseEntity.ok(percentage);
    }

    @GetMapping("/class/{classId}/percentage")
    @Operation(summary = "Get class attendance percentage", description = "Calculates attendance percentage for a class")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Double> getClassAttendancePercentage(
            @Parameter(description = "Class ID") @PathVariable UUID classId,
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        double percentage = attendanceService.getAttendancePercentageByClass(classId, startDate, endDate);
        return ResponseEntity.ok(percentage);
    }

    @GetMapping("/student/{studentId}/present-count")
    @Operation(summary = "Count present days for student", description = "Returns total present days for a student")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Long> countPresentDaysByStudent(
            @Parameter(description = "Student ID") @PathVariable UUID studentId) {
        long count = attendanceService.countPresentDaysByStudent(studentId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/student/{studentId}/absent-count")
    @Operation(summary = "Count absent days for student", description = "Returns total absent days for a student")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Long> countAbsentDaysByStudent(
            @Parameter(description = "Student ID") @PathVariable UUID studentId) {
        long count = attendanceService.countAbsentDaysByStudent(studentId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/class/{classId}/date/{date}/present-count")
    @Operation(summary = "Count present students by class and date", description = "Returns count of present students for a class on a specific date")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Long> countPresentStudentsByClassAndDate(
            @Parameter(description = "Class ID") @PathVariable UUID classId,
            @Parameter(description = "Date") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        long count = attendanceService.countPresentStudentsByClassAndDate(classId, date);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/class/{classId}/date/{date}/total-count")
    @Operation(summary = "Count total students by class and date", description = "Returns total count of students for a class on a specific date")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER') or hasRole('REGISTRAR')")
    public ResponseEntity<Long> countTotalStudentsByClassAndDate(
            @Parameter(description = "Class ID") @PathVariable UUID classId,
            @Parameter(description = "Date") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        long count = attendanceService.countTotalStudentsByClassAndDate(classId, date);
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete attendance", description = "Deletes an attendance record")
    @ApiResponse(responseCode = "204", description = "Attendance deleted successfully")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAttendance(@Parameter(description = "Attendance ID") @PathVariable UUID id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }
}
