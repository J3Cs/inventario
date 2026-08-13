package com.j3cs.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j3cs.inventario.dto.RolRequestDTO;
import com.j3cs.inventario.dto.RolResponseDTO;
import com.j3cs.inventario.service.RolService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolController.class)
@AutoConfigureMockMvc(addFilters = false)
class RolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RolService rolService;

    @Test
    @DisplayName("GET /api/roles - Debe retornar lista de roles")
    void listarRoles_RetornaHttp200() throws Exception {
        given(rolService.listarTodos()).willReturn(List.of(
                new RolResponseDTO(1, "Administrador", "Acceso total"),
                new RolResponseDTO(2, "Almacenista", "Gestión de stock")
        ));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Administrador"));
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("POST /api/roles - Debe crear un nuevo rol")
    void crearRol_RetornaHttp201() throws Exception {
        RolRequestDTO request = new RolRequestDTO("Auditor", "Lectura e informes");
        RolResponseDTO response = new RolResponseDTO(3, "Auditor", "Lectura e informes");

        given(rolService.crearRol(any(RolRequestDTO.class))).willReturn(response);

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nombre").value("Auditor"));
    }
}