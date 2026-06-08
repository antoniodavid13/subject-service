package com.adfdev.subject_service.controller;

import com.adfdev.subject_service.dto.request.SubjectRequest;
import com.adfdev.subject_service.dto.response.SubjectResponse;
import com.adfdev.subject_service.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<SubjectResponse>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getSubjectById(@PathVariable String id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<SubjectResponse>> getSubjectsByTeacher(@PathVariable String teacherId) {
        return ResponseEntity.ok(subjectService.getSubjectsByTeacher(teacherId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<SubjectResponse> createSubject(
            @Valid @RequestBody SubjectRequest request,
            Authentication authentication) {
        String teacherId = (String) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(subjectService.createSubject(request, teacherId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<SubjectResponse> updateSubject(
            @PathVariable String id,
            @Valid @RequestBody SubjectRequest request,
            Authentication authentication) {
        String teacherId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(subjectService.updateSubject(id, request, teacherId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
    public ResponseEntity<Void> deleteSubject(
            @PathVariable String id,
            Authentication authentication) {
        String teacherId = (String) authentication.getPrincipal();
        subjectService.deleteSubject(id, teacherId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<SubjectResponse>> getSubjectsByClass(@PathVariable String classId) {
        return ResponseEntity.ok(subjectService.getSubjectsByClass(classId));
    }
}