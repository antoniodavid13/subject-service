package com.adfdev.subject_service.repository;

import com.adfdev.subject_service.entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, String> {
    List<Class> findByCreatedBy(String createdBy);
}