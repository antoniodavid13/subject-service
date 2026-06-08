package com.adfdev.subject_service.service;

import com.adfdev.subject_service.dto.request.SubjectRequest;
import com.adfdev.subject_service.dto.response.SubjectResponse;
import com.adfdev.subject_service.entity.Subject;
import com.adfdev.subject_service.exception.SubjectNotFoundException;
import com.adfdev.subject_service.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(SubjectResponse::from)
                .toList();
    }

    public SubjectResponse getSubjectById(String id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException("Asignatura no encontrada: " + id));
        return SubjectResponse.from(subject);
    }

    public List<SubjectResponse> getSubjectsByTeacher(String teacherId) {
        return subjectRepository.findByTeacherId(teacherId).stream()
                .map(SubjectResponse::from)
                .toList();
    }

    @Transactional
    public SubjectResponse createSubject(SubjectRequest request, String teacherId) {
        Subject subject = Subject.builder()
                .name(request.getName())
                .description(request.getDescription())
                .icon(request.getIcon())
                .teacherId(teacherId)
                .classId(request.getClassId())
                .build();
        subject = subjectRepository.save(subject);
        log.info("Asignatura creada: {} por teacher {}", subject.getName(), teacherId);
        return SubjectResponse.from(subject);
    }

    @Transactional
    public SubjectResponse updateSubject(String id, SubjectRequest request, String teacherId) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException("Asignatura no encontrada: " + id));

        if (!subject.getTeacherId().equals(teacherId)) {
            throw new RuntimeException("No tienes permiso para editar esta asignatura");
        }

        // Validar nombre único si cambió
        if (!subject.getName().equals(request.getName()) && subjectRepository.existsByName(request.getName())) {
            throw new RuntimeException("Ya existe una asignatura con el nombre: " + request.getName());
        }

        subject.setName(request.getName());
        subject.setDescription(request.getDescription());
        subject.setIcon(request.getIcon());
        subject = subjectRepository.save(subject);
        log.info("Asignatura actualizada: {}", subject.getName());
        return SubjectResponse.from(subject);
    }

    @Transactional
    public void deleteSubject(String id, String teacherId) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException("Asignatura no encontrada: " + id));

        if (!subject.getTeacherId().equals(teacherId)) {
            throw new RuntimeException("No tienes permiso para eliminar esta asignatura");
        }

        subjectRepository.delete(subject);
        log.info("Asignatura eliminada: {}", id);
    }
    public List<SubjectResponse> getSubjectsByClass(String classId) {
        return subjectRepository.findByClassId(classId).stream()
                .map(SubjectResponse::from)
                .toList();
    }

}