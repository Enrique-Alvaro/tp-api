package com.example.Marketplace.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoResponseDTO {
    private Long productoId;
    private String nombre;
    private String imagenUrl;
    private BigDecimal precioUnitario;
    private int cantidad;
    private BigDecimal subtotal;
    private boolean disponible;
}