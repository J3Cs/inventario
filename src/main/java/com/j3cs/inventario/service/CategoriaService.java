package com.j3cs.inventario.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.j3cs.inventario.dto.CategoriaRequestDTO;
import com.j3cs.inventario.dto.CategoriaResponseDTO;
import com.j3cs.inventario.model.Categoria;
import com.j3cs.inventario.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listar() {
        return categoriaRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO obtenerCategoriaPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .map(categoria -> new CategoriaResponseDTO(categoria.getId(), categoria.getNombre(),
                        categoria.getDescripcion()))
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con nombre: " + nombre));
    }

    @Transactional
    public CategoriaResponseDTO crearCategoria(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByNombre(dto.nombre())) {
            throw new IllegalArgumentException("La categoria con ese nombre ya existe");
        }

        Categoria categoria = Categoria.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .build();

        return mapToDTO(categoriaRepository.save(categoria));
    }

    private CategoriaResponseDTO mapToDTO(Categoria categoria) {
        return new CategoriaResponseDTO(categoria.getId(), categoria.getNombre(), categoria.getDescripcion());
    }
}
