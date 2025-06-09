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

    // Vendedor es obligatorio y cada producto debe tener un vendedor asociado
    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Usuario vendedor;

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;
}
