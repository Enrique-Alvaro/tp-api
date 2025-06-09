package com.example.Marketplace.Service.Vendedor;

import com.example.Marketplace.DTO.VentasVendedorDTO;
import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Entity.VentaDetalle;
import com.example.Marketplace.Repository.VentaDetalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentasServiceImpl implements VentasService {

    private final VentaDetalleRepository ventaDetalleRepository;

    @Override
    public VentasVendedorDTO getVentasByVendedor(Usuario vendedor) {
        return getVentasByVendedorId(vendedor.getId());
    }

    @Override
    public VentasVendedorDTO getVentasByVendedorId(Long vendedorId) {
        // Obtener ventas ordenadas por fecha
        List<VentaDetalle> ventasDetalle = ventaDetalleRepository.findByVendedorIdOrderByFechaVentaDesc(vendedorId);

        // Si no hay ventas, retornar respuesta vacía
        if (ventasDetalle.isEmpty()) {
            return VentasVendedorDTO.builder()
                    .totalVentas(BigDecimal.ZERO)
                    .totalProductosVendidos(0)
                    .ventas(Collections.emptyList())
                    .build();
        }

        // Calcular totales
        BigDecimal totalVentas = ventasDetalle.stream()
                .map(VentaDetalle::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalProductosVendidos = ventasDetalle.stream()
                .mapToInt(VentaDetalle::getCantidad)
                .sum();

        // Convertir las ventas a DTOs
        List<VentasVendedorDTO.VentaDetalleDTO> ventasDTO = ventasDetalle.stream()
                .map(venta -> VentasVendedorDTO.VentaDetalleDTO.builder()
                        .nombreProducto(venta.getNombreProducto())
                        .cantidad(venta.getCantidad())
                        .precioUnitario(venta.getPrecioUnitario())
                        .subtotal(venta.getSubtotal())
                        .fechaVenta(venta.getFechaVenta())
                        .estado(venta.getEstadoOrden())
                        .build())
                .collect(Collectors.toList());

        // Construir y retornar el DTO final
        return VentasVendedorDTO.builder()
                .totalVentas(totalVentas)
                .totalProductosVendidos(totalProductosVendidos)
                .ventas(ventasDTO)
                .build();
    }

}