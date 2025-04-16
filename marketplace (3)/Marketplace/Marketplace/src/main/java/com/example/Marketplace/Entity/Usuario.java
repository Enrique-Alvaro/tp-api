package com.example.Marketplace.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Enumerated(EnumType.STRING)
    private Rol role;

    @OneToMany(mappedBy = "usuario")
    private List<Orden> orders;

    // Relación con Carrito
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Carrito carrito;
}
