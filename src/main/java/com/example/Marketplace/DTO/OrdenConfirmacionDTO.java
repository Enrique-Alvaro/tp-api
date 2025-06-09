package com.example.Marketplace.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la confirmación de orden antes de procesar el pago
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdenConfirmacionDTO {
    private List<ItemResumenDTO> items;
    private String direccionEnvio;
    private String metodoPago;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private String codigoDescuento;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemResumenDTO {
        private Long productoId;
        private String nombre;
        private int cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}
