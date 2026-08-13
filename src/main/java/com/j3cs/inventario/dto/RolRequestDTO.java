package com.j3cs.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RolRequestDTO(
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    String nombre,

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    String descripcion
) {}