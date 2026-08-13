package com.j3cs.inventario.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "proveedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "contacto_nombre", length = 100)
    private String contactoNombre;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;
}
