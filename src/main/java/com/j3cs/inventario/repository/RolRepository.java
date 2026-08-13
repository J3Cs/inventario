package com.j3cs.inventario.repository;

import com.j3cs.inventario.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    boolean existsByNombre(String nombre);
    Optional<Rol> findByNombre(String nombre);
}
