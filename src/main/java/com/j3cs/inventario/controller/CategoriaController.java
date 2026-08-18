package com.j3cs.inventario.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.j3cs.inventario.dto.CategoriaRequestDTO;
import com.j3cs.inventario.dto.CategoriaResponseDTO;
import com.j3cs.inventario.service.CategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor 
public class CategoriaController {

    private final CategoriaService categoriaService; 
    
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<CategoriaResponseDTO> getByName(@PathVariable String nombre){
        return ResponseEntity.ok(categoriaService.obtenerCategoriaPorNombre(nombre));
    }
    
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> create(@Valid @RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crearCategoria(dto));
    }
    
}
