package com.adfdev.subject_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubjectRequest {

    @NotBlank(message = "El nombre de la asignatura es obligatorio")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres")
    private String name;

    private String classId;

    private String description;

    @Size(max = 100, message = "El icono no puede superar los 100 caracteres")
    private String icon;
}