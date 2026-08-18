package com.j3cs.inventario.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.j3cs.inventario.dto.CategoriaRequestDTO;
import com.j3cs.inventario.dto.CategoriaResponseDTO;
import com.j3cs.inventario.model.Categoria;
import com.j3cs.inventario.repository.CategoriaRepository;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private CategoriaRequestDTO requestDTO;
    private Categoria categoriaGuardada;

    @BeforeEach
    void setup() {
        requestDTO = new CategoriaRequestDTO(
                "Electronica",
                "Insumnos electronicos");

        categoriaGuardada = Categoria.builder()
                .id(1)
                .nombre("Electronica")
                .descripcion("Insumos Electronicos")
                .build();
    }

    @Test
    @DisplayName("Debe registrar una categoria cuando los datos son validos y el nombre no existe")
    void registrarCategoria_Exito() {
        given(categoriaRepository.existsByNombre("Electronica")).willReturn(false);
        given(categoriaRepository.save(any(Categoria.class))).willReturn(categoriaGuardada);

        CategoriaResponseDTO resultado = categoriaService.crearCategoria(requestDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1);
        assertThat(resultado.nombre()).isEqualTo("Electronica");
        assertThat(resultado.descripcion()).isEqualTo("Insumos Electronicos");

    }

    @Test
    @DisplayName("Debe lanzar excepcion si el nombre de la Categoria ya existe")
    void registrarCategoria_existente_lanzaException(){
        given(categoriaRepository.existsByNombre("Electronica")).willReturn(true);

        assertThatThrownBy(() -> categoriaService.crearCategoria(requestDTO))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("La categoria con ese nombre ya existe");
    }

    @Test
    @DisplayName("Debe listar todas las categorias")
    void listarCategorias_Exito() {
        given(categoriaRepository.findAll()).willReturn(List.of(categoriaGuardada));

        List<CategoriaResponseDTO> categorias = categoriaService.listar();

        assertThat(categorias).hasSize(1);
        assertThat(categorias.get(0).nombre()).isEqualTo("Electronica");
    }

}
