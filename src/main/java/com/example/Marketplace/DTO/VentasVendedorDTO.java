package com.example.Marketplace.DTO;

import com.example.Marketplace.Entity.EstadoOrden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentasVendedorDTO {
    private BigDecimal totalVentas;
    private int totalProductosVendidos;
    private List<VentaDetalleDTO> ventas;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VentaDetalleDTO {
        private String nombreProducto;
        private int cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
        private LocalDateTime fechaVenta;
        private EstadoOrden estado;
    }
}