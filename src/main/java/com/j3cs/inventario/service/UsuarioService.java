package com.j3cs.inventario.service;

import com.j3cs.inventario.dto.UsuarioRequestDTO;
import com.j3cs.inventario.dto.UsuarioResponseDTO;
import com.j3cs.inventario.model.Rol;
import com.j3cs.inventario.model.Usuario;
import com.j3cs.inventario.repository.RolRepository;
import com.j3cs.inventario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));
        return mapToDTO(usuario);
    }

    @Transactional
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("El email ya se encuentra registrado: " + dto.email());
        }

        Rol rol = rolRepository.findById(dto.rolId())
                .orElseThrow(() -> new IllegalArgumentException("No existe el rol con ID: " + dto.rolId()));

        // Hash temporal simulado; más adelante se integrará con PasswordEncoder de Spring Security
        String passwordHash = "hashed_" + dto.password();

        Usuario usuario = Usuario.builder()
                .rol(rol)
                .nombre(dto.nombre())
                .email(dto.email())
                .passwordHash(passwordHash)
                .activo(true)
                .build();

        return mapToDTO(usuarioRepository.save(usuario));
    }

    private UsuarioResponseDTO mapToDTO(Usuario u) {
        return new UsuarioResponseDTO(
                u.getId(),
                u.getRol().getId(),
                u.getRol().getNombre(),
                u.getNombre(),
                u.getEmail(),
                u.getActivo(),
                u.getCreadoEn()
        );
    }
}