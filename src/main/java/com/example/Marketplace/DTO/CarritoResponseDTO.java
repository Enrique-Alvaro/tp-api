package com.example.Marketplace.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
public class CarritoResponseDTO {
    private Long id;
    private List<ItemCarritoResponseDTO> items;
    private BigDecimal total;
    
    @Data
    @Builder
    public static class ItemCarritoResponseDTO {
        private Long productoId;
        private String nombreProducto;
        private String imagenProducto; 
        private BigDecimal precioUnitario;
        private int cantidad;
        private BigDecimal subtotal;
    }
}
