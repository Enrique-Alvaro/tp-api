package com.example.Marketplace.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrden> items = new ArrayList<>();
    
    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private BigDecimal subtotal;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;
    
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    
    private String direccionEnvio;
    private String metodoPago;
    private String numeroFactura; // Número único de factura
    private String codigoDescuento;
    
    @PrePersist
    public void generarNumeroFactura() {
        this.numeroFactura = "FAC-" + LocalDateTime.now().getYear() + "-" + String.format("%06d", this.id);
    }

    
}

