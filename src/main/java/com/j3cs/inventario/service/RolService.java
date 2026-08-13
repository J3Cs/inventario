package com.j3cs.inventario.service;

import com.j3cs.inventario.dto.RolRequestDTO;
import com.j3cs.inventario.dto.RolResponseDTO;
import com.j3cs.inventario.model.Rol;
import com.j3cs.inventario.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;

    @Transactional(readOnly = true)
    public List<RolResponseDTO> listarTodos() {
        return rolRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public RolResponseDTO obtenerPorId(Integer id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado con ID: " + id));
        return mapToDTO(rol);
    }

    @Transactional
    public RolResponseDTO crearRol(RolRequestDTO dto) {
        if (rolRepository.existsByNombre(dto.nombre())) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + dto.nombre());
        }

        Rol rol = Rol.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .build();

        return mapToDTO(rolRepository.save(rol));
    }

    private RolResponseDTO mapToDTO(Rol rol) {
        return new RolResponseDTO(rol.getId(), rol.getNombre(), rol.getDescripcion());
    }
}