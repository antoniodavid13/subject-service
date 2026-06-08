package com.adfdev.subject_service.controller;

import com.adfdev.subject_service.dto.request.EnrollmentRequest;
import com.adfdev.subject_service.dto.response.EnrollmentResponse;
import com.adfdev.subject_service.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<EnrollmentResponse> enroll(
            @Valid @RequestBody EnrollmentRequest request,
            Authentication authentication) {
        String studentId = (String) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enroll(studentId, request.getSubjectId()));
    }

    @DeleteMapping("/{subjectId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> unenroll(
            @PathVariable String subjectId,
            Authentication authentication) {
        String studentId = (String) authentication.getPrincipal();
        enrollmentService.unenroll(studentId, subjectId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EnrollmentResponse>> getMyEnrollments(
            Authentication authentication) {
        String studentId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsBySubject(
            @PathVariable String subjectId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsBySubject(subjectId));
    }
}