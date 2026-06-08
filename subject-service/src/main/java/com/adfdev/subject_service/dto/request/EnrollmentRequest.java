package com.adfdev.subject_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnrollmentRequest {

    @NotBlank(message = "El ID de la asignatura es obligatorio")
    private String subjectId;
}