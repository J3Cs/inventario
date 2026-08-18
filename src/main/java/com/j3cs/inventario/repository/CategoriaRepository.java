package com.j3cs.inventario.repository;

import com.j3cs.inventario.model.Categoria;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    boolean existsByNombre(String nombre);
    Optional<Categoria> findByNombre(String nombre);
}
