package com.adfdev.subject_service.dto.response;

import com.adfdev.subject_service.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponse {

    private String id;
    private String name;
    private String description;
    private String icon;
    private String teacherId;
    private LocalDateTime createdAt;
    private String classId;

    public static SubjectResponse from(Subject subject) {
        return SubjectResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .description(subject.getDescription())
                .icon(subject.getIcon())
                .teacherId(subject.getTeacherId())
                .createdAt(subject.getCreatedAt())
                .classId(subject.getClassId())
                .build();
    }
}