package com.adfdev.subject_service.service;

import com.adfdev.subject_service.dto.response.EnrollmentResponse;
import com.adfdev.subject_service.entity.Enrollment;
import com.adfdev.subject_service.entity.Subject;
import com.adfdev.subject_service.exception.EnrollmentAlreadyExistsException;
import com.adfdev.subject_service.exception.EnrollmentNotFoundException;
import com.adfdev.subject_service.exception.SubjectNotFoundException;
import com.adfdev.subject_service.repository.EnrollmentRepository;
import com.adfdev.subject_service.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public EnrollmentResponse enroll(String studentId, String subjectId) {
        com.adfdev.subject_service.entity.Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new SubjectNotFoundException("Asignatura no encontrada: " + subjectId));

        if (enrollmentRepository.existsByStudentIdAndSubjectId(studentId, subjectId)) {
            throw new EnrollmentAlreadyExistsException("Ya estás matriculado en esta asignatura");
        }

        Enrollment enrollment = Enrollment.builder()
                .studentId(studentId)
                .subject(subject)
                .build();
        enrollment = enrollmentRepository.save(enrollment);
        log.info("Estudiante {} matriculado en {}", studentId, subject.getName());
        return EnrollmentResponse.from(enrollment);
    }

    @Transactional
    public void unenroll(String studentId, String subjectId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndSubjectId(studentId, subjectId)
                .orElseThrow(() -> new EnrollmentNotFoundException("No estás matriculado en esta asignatura"));
        enrollmentRepository.delete(enrollment);
        log.info("Estudiante {} desmatriculado de asignatura {}", studentId, subjectId);
    }

    public List<EnrollmentResponse> getEnrollmentsByStudent(String studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(EnrollmentResponse::from)
                .toList();
    }

    public List<EnrollmentResponse> getEnrollmentsBySubject(String subjectId) {
        return enrollmentRepository.findBySubjectId(subjectId).stream()
                .map(EnrollmentResponse::from)
                .toList();
    }
}