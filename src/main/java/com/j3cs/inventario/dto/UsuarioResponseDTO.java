package com.j3cs.inventario.dto;

import java.time.LocalDateTime;

public record UsuarioResponseDTO(
    Integer id,
    Integer rolId,
    String rolNombre,
    String nombre,
    String email,
    Boolean activo,
    LocalDateTime creadoEn
) {}