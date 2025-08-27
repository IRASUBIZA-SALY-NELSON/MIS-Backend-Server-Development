package com.rca.mis.repository;

import com.rca.mis.model.student.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, UUID> {

    List<StudentAttendance> findByStudent_Id(UUID studentId);
    
    List<StudentAttendance> findByClazz_Id(UUID classId);
    
    List<StudentAttendance> findBySubject_Id(UUID subjectId);
    
    List<StudentAttendance> findByDate(LocalDate date);
    
    Optional<StudentAttendance> findByStudent_IdAndDateAndSubject_Id(UUID studentId, LocalDate date, UUID subjectId);
    
    @Query("SELECT sa FROM StudentAttendance sa WHERE sa.date BETWEEN :startDate AND :endDate")
    List<StudentAttendance> findByDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT sa FROM StudentAttendance sa WHERE sa.student.id = :studentId AND sa.date BETWEEN :startDate AND :endDate")
    List<StudentAttendance> findByStudentAndDateBetween(@Param("studentId") UUID studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(sa) FROM StudentAttendance sa WHERE sa.student.id = :studentId AND sa.status = 'PRESENT'")
    long countPresentDaysByStudent(@Param("studentId") UUID studentId);
    
    @Query("SELECT COUNT(sa) FROM StudentAttendance sa WHERE sa.student.id = :studentId AND sa.status = 'ABSENT'")
    long countAbsentDaysByStudent(@Param("studentId") UUID studentId);
    
    @Query("SELECT COUNT(sa) FROM StudentAttendance sa WHERE sa.clazz.id = :classId AND sa.date = :date AND sa.status = 'PRESENT'")
    long countPresentStudentsByClassAndDate(@Param("classId") UUID classId, @Param("date") LocalDate date);
    
    @Query("SELECT COUNT(sa) FROM StudentAttendance sa WHERE sa.clazz.id = :classId AND sa.date = :date")
    long countTotalStudentsByClassAndDate(@Param("classId") UUID classId, @Param("date") LocalDate date);
}
