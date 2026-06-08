package com.adfdev.subject_service.repository;

import com.adfdev.subject_service.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {

    List<Subject> findByTeacherId(String teacherId);
    List<Subject> findByClassId(String classId);

    boolean existsByName(String name);
}