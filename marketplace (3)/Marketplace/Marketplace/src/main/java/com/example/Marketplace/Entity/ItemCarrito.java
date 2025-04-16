package com.example.Marketplace.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private BigDecimal subtotal; // Cambié el tipo de double a BigDecimal

    // Constructor adicional para crear ItemCarrito con solo productoId y cantidad
    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        // Usando BigDecimal para el precio y cantidad
        this.subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }
}
