package com.j3cs.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j3cs.inventario.dto.UsuarioRequestDTO;
import com.j3cs.inventario.dto.UsuarioResponseDTO;
import com.j3cs.inventario.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    @DisplayName("GET /api/usuarios - Debe retornar la lista de usuarios")
    void listarUsuarios_RetornaHttp200() throws Exception {
        given(usuarioService.listarTodos()).willReturn(List.of(
                new UsuarioResponseDTO(1, 1, "Administrador", "Admin Sistema", "admin@empresa.com", true, LocalDateTime.now())
        ));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("admin@empresa.com"));
    }

    @Test
    @DisplayName("POST /api/usuarios - Debe registrar un nuevo usuario y retornar status 201")
    void registrarUsuario_RetornaHttp201() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO(1, "Admin Sistema", "admin@empresa.com", "Password123");
        UsuarioResponseDTO response = new UsuarioResponseDTO(1, 1, "Administrador", "Admin Sistema", "admin@empresa.com", true, LocalDateTime.now());

        given(usuarioService.registrarUsuario(any(UsuarioRequestDTO.class))).willReturn(response);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("admin@empresa.com"));
    }
}