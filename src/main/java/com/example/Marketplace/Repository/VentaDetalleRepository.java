package com.example.Marketplace.Repository;

import com.example.Marketplace.Entity.EstadoOrden;
import com.example.Marketplace.Entity.VentaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, Long> {
    
    // Encontrar ventas por ID de vendedor
    List<VentaDetalle> findByVendedorId(Long vendedorId);
    
    // Encontrar ventas por ID de producto
    List<VentaDetalle> findByProductoId(Long productoId);
    
    // Encontrar ventas por ID de vendedor y estado de orden
    List<VentaDetalle> findByVendedorIdAndEstadoOrden(Long vendedorId, EstadoOrden estadoOrden);
    
    // Encontrar ventas por ID de producto y estado de orden
    List<VentaDetalle> findByProductoIdAndEstadoOrden(Long productoId, EstadoOrden estadoOrden);
    
    // Encontrar ventas por ID de vendedor ordenadas por fecha (más recientes primero)
    List<VentaDetalle> findByVendedorIdOrderByFechaVentaDesc(Long vendedorId);
    
    // Consulta para obtener total de ventas y productos vendidos por vendedor
    @Query("SELECT SUM(v.subtotal) as totalVentas, SUM(v.cantidad) as totalProductos " +
           "FROM VentaDetalle v WHERE v.vendedor.id = :vendedorId AND v.estadoOrden <> 'CANCELADA'")
    Object[] getTotalesVentasYProductosPorVendedor(Long vendedorId);
    
    // Contar cantidad total vendida para un producto específico
    @Query("SELECT SUM(v.cantidad) FROM VentaDetalle v WHERE v.producto.id = :productoId AND v.estadoOrden <> 'CANCELADA'")
    Integer getCantidadTotalVendidaPorProducto(Long productoId);
    
    // Calcular total vendido para un producto específico
    @Query("SELECT SUM(v.subtotal) FROM VentaDetalle v WHERE v.producto.id = :productoId AND v.estadoOrden <> 'CANCELADA'")
    BigDecimal getTotalVendidoPorProducto(Long productoId);
    
    // Encontrar ventas por lista de IDs de productos
    List<VentaDetalle> findByProductoIdIn(List<Long> productosIds);
}
