package com.j3cs.inventario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j3cs.inventario.dto.CategoriaRequestDTO;
import com.j3cs.inventario.dto.CategoriaResponseDTO;
import com.j3cs.inventario.service.CategoriaService;

@WebMvcTest(CategoriaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoriaService categoriaService;

    @Test
    @DisplayName("GET /api/categorias - Debe devolver la lista de categorias")
    void listarCategorias_ReturnHttp200() throws Exception {
        given(categoriaService.listar()).willReturn(List.of(
                new CategoriaResponseDTO(1, "Electronica", "Componentes de electronica"),
                new CategoriaResponseDTO(2, "Oficina", "Equipo de oficina")));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Electronica"));
    }

    @Test
    @DisplayName("POST /api/categorias - Debe crear una nueva categoria")
    void createCategoria_ReturnHttp201() throws Exception {
        CategoriaRequestDTO request = new CategoriaRequestDTO("Oficina", "Equipo de oficina");
        CategoriaResponseDTO response = new CategoriaResponseDTO(3, "Oficina", "Equipo de oficina");

        given(categoriaService.crearCategoria(any(CategoriaRequestDTO.class))).willReturn(response);

        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nombre").value("Oficina"))
                .andExpect(jsonPath("$.descripcion").value("Equipo de oficina"));

    }

    @Test
    @DisplayName("GET /api/categorias/{nombre} - Debe devolver la categoria cuando existe")
    void getCategoryByName_ReturnHttp200() throws Exception {
        String searchName = "Electronica";
        CategoriaResponseDTO responseDTO = new CategoriaResponseDTO(1, "Electronica", "Componentes de electronica");

        given(categoriaService.obtenerCategoriaPorNombre(searchName)).willReturn(responseDTO);

        mockMvc.perform(get("/api/categorias/{nombre}", searchName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Electronica"))
                .andExpect(jsonPath("$.descripcion").value("Componentes de electronica"));

    }
}
