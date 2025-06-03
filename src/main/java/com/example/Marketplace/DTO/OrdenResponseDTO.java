package com.example.Marketplace.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrdenResponseDTO {
    private Long id;
    private String numeroFactura;
    private LocalDateTime fechaCreacion;
    private String estado;
    private BigDecimal total;
    private BigDecimal subtotal;
    private String direccionEnvio;
    private String metodoPago;
    private String codigoDescuento;
    private List<ItemOrdenDTO> items;
    
    @Data
    @Builder
    public static class ItemOrdenDTO {
        private Long productoId;
        private String nombreProducto;
        private int cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}