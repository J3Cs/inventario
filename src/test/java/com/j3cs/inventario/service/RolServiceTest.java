package com.j3cs.inventario.service;

import com.j3cs.inventario.dto.RolRequestDTO;
import com.j3cs.inventario.dto.RolResponseDTO;
import com.j3cs.inventario.model.Rol;
import com.j3cs.inventario.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RolServiceTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolService rolService;

    private RolRequestDTO requestDTO;
    private Rol rolGuardado;

    @BeforeEach
    void setUp() {
        requestDTO = new RolRequestDTO("Supervisión", "Encargado de la auditoría de inventario");
        rolGuardado = Rol.builder()
                .id(1)
                .nombre("Supervisión")
                .descripcion("Encargado de la auditoría de inventario")
                .build();
    }

    @Test
    @DisplayName("Debe crear un rol exitosamente si el nombre no existe")
    void crearRol_Exito() {
        // Given
        given(rolRepository.existsByNombre("Supervisión")).willReturn(false);
        given(rolRepository.save(any(Rol.class))).willReturn(rolGuardado);

        // When
        RolResponseDTO resultado = rolService.crearRol(requestDTO);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1);
        assertThat(resultado.nombre()).isEqualTo("Supervisión");
    }

    @Test
    @DisplayName("Debe lanzar excepción si intenta crear un rol con nombre duplicado")
    void crearRol_NombreDuplicado_LanzaExcepcion() {
        // Given
        given(rolRepository.existsByNombre("Supervisión")).willReturn(true);

        // When / Then
        assertThatThrownBy(() -> rolService.crearRol(requestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ya existe un rol con el nombre: Supervisión");
    }

    @Test
    @DisplayName("Debe listar todos los roles")
    void listarRoles_Exito() {
        // Given
        given(rolRepository.findAll()).willReturn(List.of(rolGuardado));

        // When
        List<RolResponseDTO> roles = rolService.listarTodos();

        // Then
        assertThat(roles).hasSize(1);
        assertThat(roles.get(0).nombre()).isEqualTo("Supervisión");
    }
}