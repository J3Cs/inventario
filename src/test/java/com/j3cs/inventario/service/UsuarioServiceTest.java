package com.j3cs.inventario.service;

import com.j3cs.inventario.dto.UsuarioRequestDTO;
import com.j3cs.inventario.dto.UsuarioResponseDTO;
import com.j3cs.inventario.model.Rol;
import com.j3cs.inventario.model.Usuario;
import com.j3cs.inventario.repository.RolRepository;
import com.j3cs.inventario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Rol rolAdmin;
    private UsuarioRequestDTO requestDTO;
    private Usuario usuarioGuardado;

    @BeforeEach
    void setUp() {
        rolAdmin = Rol.builder()
                .id(1)
                .nombre("Administrador")
                .descripcion("Acceso total al sistema")
                .build();

        requestDTO = new UsuarioRequestDTO(
                1,
                "Admin Sistema",
                "admin@empresa.com",
                "Password123"
        );

        usuarioGuardado = Usuario.builder()
                .id(1)
                .rol(rolAdmin)
                .nombre("Admin Sistema")
                .email("admin@empresa.com")
                .passwordHash("hashed_Password123")
                .activo(true)
                .creadoEn(LocalDateTime.now())
                .build();
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Debe registrar un usuario correctamente cuando los datos son válidos y el email no existe")
    void registrarUsuario_Exito() {
        // Given
        given(usuarioRepository.existsByEmail("admin@empresa.com")).willReturn(false);
        given(rolRepository.findById(1)).willReturn(Optional.of(rolAdmin));
        given(usuarioRepository.save(any(Usuario.class))).willReturn(usuarioGuardado);

        // When
        UsuarioResponseDTO resultado = usuarioService.registrarUsuario(requestDTO);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1);
        assertThat(resultado.email()).isEqualTo("admin@empresa.com");
        assertThat(resultado.rolNombre()).isEqualTo("Administrador");
        assertThat(resultado.activo()).isTrue();
    }

    @Test
    @DisplayName("Debe lanzar excepción si el email ya está registrado")
    void registrarUsuario_EmailDuplicado_LanzaExcepcion() {
        // Given
        given(usuarioRepository.existsByEmail("admin@empresa.com")).willReturn(true);

        // When / Then
        assertThatThrownBy(() -> usuarioService.registrarUsuario(requestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("El email ya se encuentra registrado: admin@empresa.com");
    }

    @Test
    @DisplayName("Debe lanzar excepción si el ID de rol no existe")
    void registrarUsuario_RolInexistente_LanzaExcepcion() {
        // Given
        given(usuarioRepository.existsByEmail("admin@empresa.com")).willReturn(false);
        given(rolRepository.findById(1)).willReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> usuarioService.registrarUsuario(requestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No existe el rol con ID: 1");
    }

    @Test
    @DisplayName("Debe listar todos los usuarios")
    void listarUsuarios_Exito() {
        // Given
        given(usuarioRepository.findAll()).willReturn(List.of(usuarioGuardado));

        // When
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();

        // Then
        assertThat(usuarios).hasSize(1);
        assertThat(usuarios.get(0).email()).isEqualTo("admin@empresa.com");
    }
}