package com.rca.mis.repository;

import com.rca.mis.model.student.StudentMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentMarksRepository extends JpaRepository<StudentMarks, UUID> {

    List<StudentMarks> findByStudent_Id(UUID studentId);
    
    @Query("SELECT sm FROM StudentMarks sm WHERE sm.assessment.subject.id = :subjectId")
    List<StudentMarks> findBySubject_Id(@Param("subjectId") UUID subjectId);
    
    List<StudentMarks> findByAssessment_Id(UUID assessmentId);
    
    @Query("SELECT sm FROM StudentMarks sm WHERE sm.assessment.term.id = :termId")
    List<StudentMarks> findByTerm_Id(@Param("termId") UUID termId);
    
    @Query("SELECT sm FROM StudentMarks sm WHERE sm.student.id = :studentId AND sm.assessment.subject.id = :subjectId AND sm.assessment.id = :assessmentId")
    Optional<StudentMarks> findByStudent_IdAndSubject_IdAndAssessment_Id(@Param("studentId") UUID studentId, @Param("subjectId") UUID subjectId, @Param("assessmentId") UUID assessmentId);
    
    @Query("SELECT sm FROM StudentMarks sm WHERE sm.student.id = :studentId AND sm.assessment.term.id = :termId")
    List<StudentMarks> findByStudentAndTerm(@Param("studentId") UUID studentId, @Param("termId") UUID termId);
    
    @Query("SELECT sm FROM StudentMarks sm WHERE sm.student.id = :studentId AND sm.assessment.subject.id = :subjectId")
    List<StudentMarks> findByStudentAndSubject(@Param("studentId") UUID studentId, @Param("subjectId") UUID subjectId);
    
    @Query("SELECT AVG(sm.marksObtained) FROM StudentMarks sm WHERE sm.student.id = :studentId AND sm.assessment.term.id = :termId")
    Double getAverageMarksByStudentAndTerm(@Param("studentId") UUID studentId, @Param("termId") UUID termId);
    
    @Query("SELECT AVG(sm.marksObtained) FROM StudentMarks sm WHERE sm.assessment.subject.id = :subjectId AND sm.assessment.term.id = :termId")
    Double getAverageMarksBySubjectAndTerm(@Param("subjectId") UUID subjectId, @Param("termId") UUID termId);
    
    @Query("SELECT MAX(sm.marksObtained) FROM StudentMarks sm WHERE sm.assessment.subject.id = :subjectId AND sm.assessment.id = :assessmentId")
    Double getHighestMarksBySubjectAndAssessment(@Param("subjectId") UUID subjectId, @Param("assessmentId") UUID assessmentId);
    
    @Query("SELECT MIN(sm.marksObtained) FROM StudentMarks sm WHERE sm.assessment.subject.id = :subjectId AND sm.assessment.id = :assessmentId")
    Double getLowestMarksBySubjectAndAssessment(@Param("subjectId") UUID subjectId, @Param("assessmentId") UUID assessmentId);
}
