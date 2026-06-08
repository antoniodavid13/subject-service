package com.adfdev.subject_service.service;

import com.adfdev.subject_service.entity.Class;
import com.adfdev.subject_service.entity.ClassMember;
import com.adfdev.subject_service.repository.ClassMemberRepository;
import com.adfdev.subject_service.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassService {

    private final ClassRepository classRepository;
    private final ClassMemberRepository classMemberRepository;

    // ── CRUD Clases ──────────────────────────────────────────────

    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }

    public Class getClassById(String id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada: " + id));
    }

    @Transactional
    public Class createClass(String name, String description, String createdBy) {
        Class cls = Class.builder()
                .name(name)
                .description(description)
                .createdBy(createdBy)
                .build();
        cls = classRepository.save(cls);
        log.info("Clase creada: {} por {}", name, createdBy);
        return cls;
    }

    @Transactional
    public Class updateClass(String id, String name, String description) {
        Class cls = getClassById(id);
        cls.setName(name);
        if (description != null) cls.setDescription(description);
        cls = classRepository.save(cls);
        log.info("Clase actualizada: {}", name);
        return cls;
    }

    @Transactional
    public void deleteClass(String id) {
        classRepository.deleteById(id);
        log.info("Clase eliminada: {}", id);
    }

    // ── Miembros ─────────────────────────────────────────────────

    @Transactional
    public ClassMember addMember(String classId, String userId, ClassMember.MemberRole role) {
        // Alumno solo puede estar en una clase
        if (role == ClassMember.MemberRole.student) {
            classMemberRepository.findByUserIdAndRole(userId, ClassMember.MemberRole.student)
                    .ifPresent(existing -> {
                        throw new RuntimeException("El alumno ya pertenece a una clase");
                    });
        }

        if (classMemberRepository.existsByClassIdAndUserId(classId, userId)) {
            throw new RuntimeException("El usuario ya es miembro de esta clase");
        }

        ClassMember member = ClassMember.builder()
                .classId(classId)
                .userId(userId)
                .role(role)
                .build();
        member = classMemberRepository.save(member);
        log.info("Usuario {} añadido a clase {} como {}", userId, classId, role);
        return member;
    }

    @Transactional
    public void removeMember(String classId, String userId) {
        classMemberRepository.deleteByClassIdAndUserId(classId, userId);
        log.info("Usuario {} eliminado de clase {}", userId, classId);
    }

    public List<ClassMember> getMembersByClass(String classId) {
        return classMemberRepository.findByClassId(classId);
    }

    public List<Class> getClassesByUser(String userId) {
        List<ClassMember> memberships = classMemberRepository.findByUserId(userId);
        return memberships.stream()
                .map(m -> classRepository.findById(m.getClassId()).orElse(null))
                .filter(c -> c != null)
                .toList();
    }

    public Map<String, Object> getClassDetail(String classId) {
        Class cls = getClassById(classId);
        List<ClassMember> members = classMemberRepository.findByClassId(classId);
        List<ClassMember> students = members.stream()
                .filter(m -> m.getRole() == ClassMember.MemberRole.student).toList();
        List<ClassMember> teachers = members.stream()
                .filter(m -> m.getRole() == ClassMember.MemberRole.teacher).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("id", cls.getId());
        result.put("name", cls.getName());
        result.put("description", cls.getDescription());
        result.put("createdBy", cls.getCreatedBy());
        result.put("createdAt", cls.getCreatedAt());
        result.put("studentCount", students.size());
        result.put("teacherCount", teachers.size());
        result.put("members", members.stream().map(m -> {
            Map<String, Object> member = new HashMap<>();
            member.put("userId", m.getUserId());
            member.put("role", m.getRole());
            member.put("joinedAt", m.getJoinedAt());
            return member;
        }).toList());
        return result;
    }
}