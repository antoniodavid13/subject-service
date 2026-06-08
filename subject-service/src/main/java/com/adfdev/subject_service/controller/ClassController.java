package com.adfdev.subject_service.controller;

import com.adfdev.subject_service.entity.Class;
import com.adfdev.subject_service.entity.ClassMember;
import com.adfdev.subject_service.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    // ── Admin: CRUD clases ────────────────────────────────────────

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Class>> getAllClasses() {
        return ResponseEntity.ok(classService.getAllClasses());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    public ResponseEntity<Map<String, Object>> getClassDetail(@PathVariable String id) {
        return ResponseEntity.ok(classService.getClassDetail(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Class> createClass(
            @RequestBody Map<String, String> body,
            Authentication authentication) {
        String adminId = (String) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(classService.createClass(body.get("name"), body.get("description"), adminId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Class> updateClass(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(classService.updateClass(id, body.get("name"), body.get("description")));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteClass(@PathVariable String id) {
        classService.deleteClass(id);
        return ResponseEntity.noContent().build();
    }

    // ── Miembros ──────────────────────────────────────────────────

    @PostMapping("/{classId}/members")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClassMember> addMember(
            @PathVariable String classId,
            @RequestBody Map<String, String> body) {
        ClassMember.MemberRole role = ClassMember.MemberRole.valueOf(body.get("role"));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(classService.addMember(classId, body.get("userId"), role));
    }

    @DeleteMapping("/{classId}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeMember(
            @PathVariable String classId,
            @PathVariable String userId) {
        classService.removeMember(classId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{classId}/members")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<ClassMember>> getMembers(@PathVariable String classId) {
        return ResponseEntity.ok(classService.getMembersByClass(classId));
    }

    // ── Mis clases (profesor/alumno) ──────────────────────────────

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ResponseEntity<List<Class>> getMyClasses(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(classService.getClassesByUser(userId));
    }
}