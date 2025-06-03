package com.example.Marketplace.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference // Evita la recursividad infinita al serializar Usuario desde Carrito
    @OneToOne
    @JoinColumn(name = "usuario_id") // La columna que referencia al usuario
    private Usuario usuario; // Relación bidireccional con Usuario

    @Column(nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemCarrito> items;

    public void calcularTotal() {
    this.total = items.stream()
        .map(item -> item.getProducto().getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
