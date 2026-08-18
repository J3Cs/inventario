package com.j3cs.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO(
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100, message = "El nombre de la categoría no puede exceder los 100 caracteres")
    String nombre,

    @NotBlank
    @Size(max = 500, message = "La decripcion de la categoría no puede exceder los 500 caracteres")
    String descripcion
) {}
