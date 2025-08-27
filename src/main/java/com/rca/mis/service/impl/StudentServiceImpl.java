package com.rca.mis.service.impl;

import com.rca.mis.model.student.Student;
import com.rca.mis.model.student.StudentStatus;
import com.rca.mis.repository.ClassRepository;
import com.rca.mis.repository.StudentAttendanceRepository;
import com.rca.mis.repository.StudentMarksRepository;
import com.rca.mis.repository.StudentRepository;
import com.rca.mis.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final StudentAttendanceRepository attendanceRepository;
    private final StudentMarksRepository marksRepository;

    @Override
    public Student createStudent(Student student) {
        log.info("Creating new student with email: {}", student.getUser().getEmail());
        
        if (student.getStudentCode() == null || student.getStudentCode().isEmpty()) {
            student.setStudentCode(generateStudentCode());
        }
        
        if (existsByStudentCode(student.getStudentCode())) {
            throw new IllegalArgumentException("Student code already exists: " + student.getStudentCode());
        }
        
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(UUID id, Student student) {
        log.info("Updating student with ID: {}", id);
        
        Student existingStudent = studentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));
        
        // Update fields
        existingStudent.setCurrentClass(student.getCurrentClass());
        existingStudent.setParent(student.getParent());
        existingStudent.setGuardian(student.getGuardian());
        existingStudent.setStatus(student.getStatus());
        existingStudent.setEnrollmentNumber(student.getEnrollmentNumber());
        existingStudent.setRollNumber(student.getRollNumber());
        existingStudent.setPreviousSchool(student.getPreviousSchool());
        existingStudent.setPreviousGrade(student.getPreviousGrade());
        existingStudent.setTransferDate(student.getTransferDate());
        existingStudent.setGraduationDate(student.getGraduationDate());
        existingStudent.setIsInternational(student.getIsInternational());
        existingStudent.setNationality(student.getNationality());
        existingStudent.setPassportNumber(student.getPassportNumber());
        existingStudent.setVisaNumber(student.getVisaNumber());
        existingStudent.setVisaExpiryDate(student.getVisaExpiryDate());
        existingStudent.setIsScholarship(student.getIsScholarship());
        existingStudent.setScholarshipType(student.getScholarshipType());
        existingStudent.setScholarshipAmount(student.getScholarshipAmount());
        existingStudent.setIsFinancialAid(student.getIsFinancialAid());
        existingStudent.setFinancialAidType(student.getFinancialAidType());
        existingStudent.setFinancialAidAmount(student.getFinancialAidAmount());
        existingStudent.setIsSpecialNeeds(student.getIsSpecialNeeds());
        existingStudent.setSpecialNeedsDetails(student.getSpecialNeedsDetails());
        existingStudent.setIsBoarding(student.getIsBoarding());
        existingStudent.setBoardingHouse(student.getBoardingHouse());
        existingStudent.setRoomNumber(student.getRoomNumber());
        existingStudent.setIsTransport(student.getIsTransport());
        existingStudent.setTransportRoute(student.getTransportRoute());
        existingStudent.setTransportStop(student.getTransportStop());
        existingStudent.setEmergencyContact(student.getEmergencyContact());
        existingStudent.setEmergencyContactName(student.getEmergencyContactName());
        existingStudent.setEmergencyContactRelationship(student.getEmergencyContactRelationship());
        existingStudent.setMedicalConditions(student.getMedicalConditions());
        existingStudent.setAllergies(student.getAllergies());
        existingStudent.setMedications(student.getMedications());
        existingStudent.setBloodGroup(student.getBloodGroup());
        existingStudent.setHeightCm(student.getHeightCm());
        existingStudent.setWeightKg(student.getWeightKg());
        existingStudent.setNotes(student.getNotes());
        
        return studentRepository.save(existingStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findById(UUID id) {
        return studentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findByStudentCode(String studentCode) {
        return studentRepository.findByStudentCode(studentCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findByEmail(String email) {
        return studentRepository.findByUser_Email(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Student> findAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findByStatus(StudentStatus status) {
        return studentRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findByClass(UUID classId) {
        return studentRepository.findByCurrentClass_Id(classId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findByNameContaining(String name) {
        return studentRepository.findByNameContaining(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findByAdmissionDateBetween(LocalDate startDate, LocalDate endDate) {
        return studentRepository.findByAdmissionDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findInternationalStudents() {
        return studentRepository.findInternationalStudents();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findScholarshipStudents() {
        return studentRepository.findScholarshipStudents();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findSpecialNeedsStudents() {
        return studentRepository.findSpecialNeedsStudents();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Student> findBoardingStudents() {
        return studentRepository.findBoardingStudents();
    }

    @Override
    public Student changeStatus(UUID id, StudentStatus status) {
        log.info("Changing status for student ID: {} to {}", id, status);
        
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));
        
        student.setStatus(status);
        
        if (status == StudentStatus.GRADUATED) {
            student.setGraduationDate(LocalDate.now());
        } else if (status == StudentStatus.TRANSFERRED || status == StudentStatus.WITHDRAWN) {
            student.setTransferDate(LocalDate.now());
        }
        
        return studentRepository.save(student);
    }

    @Override
    public Student assignToClass(UUID studentId, UUID classId) {
        log.info("Assigning student ID: {} to class ID: {}", studentId, classId);
        
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));
        
        var classEntity = classRepository.findById(classId)
            .orElseThrow(() -> new IllegalArgumentException("Class not found with ID: " + classId));
        
        student.setCurrentClass(classEntity);
        return studentRepository.save(student);
    }

    @Override
    public Student promoteStudent(UUID studentId, UUID newClassId) {
        log.info("Promoting student ID: {} to class ID: {}", studentId, newClassId);
        return assignToClass(studentId, newClassId);
    }

    @Override
    public void deleteStudent(UUID id) {
        log.info("Deleting student with ID: {}", id);
        
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Student not found with ID: " + id);
        }
        
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByStudentCode(String studentCode) {
        return studentRepository.existsByStudentCode(studentCode);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEnrollmentNumber(String enrollmentNumber) {
        return studentRepository.existsByEnrollmentNumber(enrollmentNumber);
    }

    @Override
    public String generateStudentCode() {
        String prefix = "STU" + Year.now().getValue();
        long count = studentRepository.count() + 1;
        return prefix + String.format("%04d", count);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(StudentStatus status) {
        return studentRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByClass(UUID classId) {
        return studentRepository.countByClassId(classId);
    }

    @Override
    @Transactional(readOnly = true)
    public double getAttendancePercentage(UUID studentId, LocalDate startDate, LocalDate endDate) {
        var attendances = attendanceRepository.findByStudentAndDateBetween(studentId, startDate, endDate);
        
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
    public double getAverageMarks(UUID studentId, UUID termId) {
        Double average = marksRepository.getAverageMarksByStudentAndTerm(studentId, termId);
        return average != null ? average : 0.0;
    }
}
