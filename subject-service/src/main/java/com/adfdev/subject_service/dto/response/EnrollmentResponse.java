package com.adfdev.subject_service.dto.response;

import com.adfdev.subject_service.entity.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {

    private String id;
    private String studentId;
    private SubjectResponse subject;
    private LocalDateTime enrolledAt;

    public static EnrollmentResponse from(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudentId())
                .subject(SubjectResponse.from(enrollment.getSubject()))
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }
}