package com.rca.mis.service;

import com.rca.mis.model.student.StudentAttendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceService {

    StudentAttendance markAttendance(StudentAttendance attendance);
    
    StudentAttendance updateAttendance(UUID id, StudentAttendance attendance);
    
    Optional<StudentAttendance> findById(UUID id);
    
    List<StudentAttendance> findByStudent(UUID studentId);
    
    List<StudentAttendance> findByClass(UUID classId);
    
    List<StudentAttendance> findBySubject(UUID subjectId);
    
    List<StudentAttendance> findByDate(LocalDate date);
    
    List<StudentAttendance> findByDateRange(LocalDate startDate, LocalDate endDate);
    
    List<StudentAttendance> findByStudentAndDateRange(UUID studentId, LocalDate startDate, LocalDate endDate);
    
    Optional<StudentAttendance> findByStudentAndDateAndSubject(UUID studentId, LocalDate date, UUID subjectId);
    
    void deleteAttendance(UUID id);
    
    long countPresentDaysByStudent(UUID studentId);
    
    long countAbsentDaysByStudent(UUID studentId);
    
    long countPresentStudentsByClassAndDate(UUID classId, LocalDate date);
    
    long countTotalStudentsByClassAndDate(UUID classId, LocalDate date);
    
    double getAttendancePercentageByStudent(UUID studentId, LocalDate startDate, LocalDate endDate);
    
    double getAttendancePercentageByClass(UUID classId, LocalDate startDate, LocalDate endDate);
}
