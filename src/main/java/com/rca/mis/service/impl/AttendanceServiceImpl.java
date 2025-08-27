package com.rca.mis.service.impl;

import com.rca.mis.model.student.StudentAttendance;
import com.rca.mis.repository.StudentAttendanceRepository;
import com.rca.mis.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final StudentAttendanceRepository attendanceRepository;

    @Override
    public StudentAttendance markAttendance(StudentAttendance attendance) {
        log.info("Marking attendance for student ID: {} on date: {}", 
                attendance.getStudent().getId(), attendance.getDate());
        
        // Check if attendance already exists for this student, date, and subject
        Optional<StudentAttendance> existing = attendanceRepository
                .findByStudent_IdAndDateAndSubject_Id(
                        attendance.getStudent().getId(),
                        attendance.getDate(),
                        attendance.getSubject().getId()
                );
        
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Attendance already marked for this student on this date and subject");
        }
        
        return attendanceRepository.save(attendance);
    }

    @Override
    public StudentAttendance updateAttendance(UUID id, StudentAttendance attendance) {
        log.info("Updating attendance with ID: {}", id);
        
        StudentAttendance existingAttendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found with ID: " + id));
        
        existingAttendance.setStatus(attendance.getStatus());
        existingAttendance.setRemarks(attendance.getRemarks());
        
        return attendanceRepository.save(existingAttendance);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentAttendance> findById(UUID id) {
        return attendanceRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentAttendance> findByStudent(UUID studentId) {
        return attendanceRepository.findByStudent_Id(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentAttendance> findByClass(UUID classId) {
        return attendanceRepository.findByClazz_Id(classId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentAttendance> findBySubject(UUID subjectId) {
        return attendanceRepository.findBySubject_Id(subjectId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentAttendance> findByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentAttendance> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentAttendance> findByStudentAndDateRange(UUID studentId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByStudentAndDateBetween(studentId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StudentAttendance> findByStudentAndDateAndSubject(UUID studentId, LocalDate date, UUID subjectId) {
        return attendanceRepository.findByStudent_IdAndDateAndSubject_Id(studentId, date, subjectId);
    }

    @Override
    public void deleteAttendance(UUID id) {
        log.info("Deleting attendance with ID: {}", id);
        
        if (!attendanceRepository.existsById(id)) {
            throw new IllegalArgumentException("Attendance not found with ID: " + id);
        }
        
        attendanceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPresentDaysByStudent(UUID studentId) {
        return attendanceRepository.countPresentDaysByStudent(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAbsentDaysByStudent(UUID studentId) {
        return attendanceRepository.countAbsentDaysByStudent(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPresentStudentsByClassAndDate(UUID classId, LocalDate date) {
        return attendanceRepository.countPresentStudentsByClassAndDate(classId, date);
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotalStudentsByClassAndDate(UUID classId, LocalDate date) {
        return attendanceRepository.countTotalStudentsByClassAndDate(classId, date);
    }

    @Override
    @Transactional(readOnly = true)
    public double getAttendancePercentageByStudent(UUID studentId, LocalDate startDate, LocalDate endDate) {
        List<StudentAttendance> attendances = findByStudentAndDateRange(studentId, startDate, endDate);
        
        if (attendances.isEmpty()) {
            return 0.0;
        }
        
        long presentDays = attendances.stream()
                .mapToLong(attendance -> attendance.isPresent() ? 1 : 0)
                .sum();
        
        return (double) presentDays / attendances.size() * 100;
    }

    @Override
    @Transactional(readOnly = true)
    public double getAttendancePercentageByClass(UUID classId, LocalDate startDate, LocalDate endDate) {
        List<StudentAttendance> attendances = attendanceRepository.findByDateBetween(startDate, endDate)
                .stream()
                .filter(attendance -> attendance.getClazz().getId().equals(classId))
                .toList();
        
        if (attendances.isEmpty()) {
            return 0.0;
        }
        
        long presentDays = attendances.stream()
                .mapToLong(attendance -> attendance.isPresent() ? 1 : 0)
                .sum();
        
        return (double) presentDays / attendances.size() * 100;
    }
}
