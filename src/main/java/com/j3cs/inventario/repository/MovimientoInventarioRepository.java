package com.j3cs.inventario.repository;

import com.j3cs.inventario.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {
    List<MovimientoInventario> findByProductoIdOrderByFechaDesc(Integer productoId);
}
