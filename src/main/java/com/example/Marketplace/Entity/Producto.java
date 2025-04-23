package com.example.Marketplace.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private BigDecimal precio;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @ElementCollection
    private List<String> imagenes; // Podés guardar URLs o paths de las imágenes

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Usuario vendedor;
}
