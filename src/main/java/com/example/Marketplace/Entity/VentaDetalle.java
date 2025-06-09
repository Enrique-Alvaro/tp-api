package com.example.Marketplace.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDetalle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Usuario vendedor;
    
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @Column(nullable = false)
    private String nombreProducto; // Mantener el nombre histórico
    
    @Column(nullable = false)
    private BigDecimal precioUnitario; // Mantener el precio histórico
    
    @Column(nullable = false)
    private int cantidad;
    
    @Column(nullable = false)
    private BigDecimal subtotal;
    
    @Column(nullable = false)
    private LocalDateTime fechaVenta;
    
    @ManyToOne
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoOrden estadoOrden;
}
