package com.adfdev.subject_service.repository;

import com.adfdev.subject_service.entity.ClassMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassMemberRepository extends JpaRepository<ClassMember, String> {
    List<ClassMember> findByClassId(String classId);
    List<ClassMember> findByUserId(String userId);
    List<ClassMember> findByClassIdAndRole(String classId, ClassMember.MemberRole role);
    Optional<ClassMember> findByUserIdAndRole(String userId, ClassMember.MemberRole role);
    boolean existsByClassIdAndUserId(String classId, String userId);
    void deleteByClassIdAndUserId(String classId, String userId);
}