package com.example.Marketplace.Service.Orden;

import com.example.Marketplace.DTO.OrdenConfirmacionDTO;
import com.example.Marketplace.DTO.OrdenRequestDTO;
import com.example.Marketplace.DTO.OrdenResponseDTO;
import com.example.Marketplace.Entity.Carrito;
import com.example.Marketplace.Entity.EstadoOrden;
import com.example.Marketplace.Entity.ItemCarrito;
import com.example.Marketplace.Entity.ItemOrden;
import com.example.Marketplace.Entity.Orden;
import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Entity.VentaDetalle;
import com.example.Marketplace.Exception.CarritoNotFoundException;
import com.example.Marketplace.Exception.OrdenNotFoundException;
import com.example.Marketplace.Exception.PagoRechazadoException;
import com.example.Marketplace.Exception.StockInsuficienteException;
import com.example.Marketplace.Repository.CarritoRepository;
import com.example.Marketplace.Repository.OrdenRepository;
import com.example.Marketplace.Repository.ProductoRepository;
import com.example.Marketplace.Repository.UsuarioRepository;
import com.example.Marketplace.Repository.VentaDetalleRepository;
import com.example.Marketplace.Service.Pago.SimuladorPago;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdenServiceImpl implements OrdenService {
    
    private final CarritoRepository carritoRepository;
    private final OrdenRepository ordenRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final VentaDetalleRepository ventaDetalleRepository;
    private final SimuladorPago simuladorPago;
        
    @Override
    @Transactional
    public OrdenResponseDTO crearOrdenDesdeCarrito(Long usuarioId, OrdenRequestDTO ordenRequest) {
        // 1. Obtener el carrito automáticamente desde el usuarioId
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
            .orElseThrow(() -> new CarritoNotFoundException("No se encontró un carrito asociado al usuario"));
        
        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }
        
        // 2. Validar stock antes de procesar
        validarStockDisponible(carrito.getItems());
        
        // 3. Calcular subtotal
        BigDecimal subtotal = carrito.getItems().stream()
            .map(item -> item.getProducto().getPrecio()
                .multiply(BigDecimal.valueOf(item.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 4. Calcular total con descuento si aplica
        BigDecimal descuentoAplicado = BigDecimal.ZERO;
        if ("PROMO2025".equalsIgnoreCase(ordenRequest.getCodigoDescuento())) {
            descuentoAplicado = subtotal.multiply(new BigDecimal("0.10"));
        }
        BigDecimal total = subtotal.subtract(descuentoAplicado);
        
        // 5. Crear la orden
        Orden orden = new Orden();
        orden.setUsuario(usuarioRepository.findById(usuarioId).orElseThrow());
        orden.setDireccionEnvio(ordenRequest.getDireccionEnvio());
        orden.setMetodoPago(ordenRequest.getMetodoPago());
        orden.setFechaCreacion(LocalDateTime.now());
        orden.setEstado(EstadoOrden.PROCESANDO_PAGO);  // Inicia en estado procesando pago
        orden.setCodigoDescuento(ordenRequest.getCodigoDescuento());
        orden.setSubtotal(subtotal);
        orden.setTotal(total);
        orden.setNotasDeEntrega(ordenRequest.getNotasDeEntrega());
        
        // 6. Crear items de la orden
        for (ItemCarrito itemCarrito : carrito.getItems()) {
            ItemOrden itemOrden = new ItemOrden();
            itemOrden.setOrden(orden);
            itemOrden.setProducto(itemCarrito.getProducto());
            itemOrden.setCantidad(itemCarrito.getCantidad());
            itemOrden.setPrecioUnitario(itemCarrito.getProducto().getPrecio());
            itemOrden.setSubtotal(itemCarrito.getProducto().getPrecio()
                .multiply(BigDecimal.valueOf(itemCarrito.getCantidad())));
            
            orden.getItems().add(itemOrden);
        }
        
        // Guardar la orden inicialmente
        Orden ordenGuardada = ordenRepository.save(orden);
        
        // 7. Procesar el pago
        String detallesPago = "";
        if ("TARJETA".equalsIgnoreCase(ordenRequest.getMetodoPago())) {
            detallesPago = "•••• " + (ordenRequest.getNumeroTarjeta() != null ? 
                ordenRequest.getNumeroTarjeta() : "1234");
        } else if ("TRANSFERENCIA".equalsIgnoreCase(ordenRequest.getMetodoPago())) {
            detallesPago = "Transferencia bancaria";
        } else {
            detallesPago = "Efectivo en entrega";
        }
        
        // Simulación del pago
        SimuladorPago.ResultadoPago resultadoPago = simuladorPago.procesarPago(
            ordenRequest.getMetodoPago(), 
            total, 
            detallesPago
        );
        
        // 8. Actualizar la orden según resultado del pago
        if (resultadoPago == SimuladorPago.ResultadoPago.APROBADO) {
            // Pago exitoso
            String codigoTransaccion = simuladorPago.generarCodigoTransaccion();
            ordenGuardada.setEstado(EstadoOrden.PAGADA);
            ordenGuardada.setCodigoTransaccion(codigoTransaccion);
            ordenGuardada.setDetallesPago(detallesPago);
            ordenGuardada.setFechaPago(LocalDateTime.now());
            ordenGuardada = ordenRepository.save(ordenGuardada);
            
            // Actualizar stock de productos y registrar las ventas
            LocalDateTime fechaVenta = LocalDateTime.now();
            for (ItemCarrito itemCarrito : carrito.getItems()) {
                Producto producto = itemCarrito.getProducto();
                
                // Actualizar stock
                producto.setStock(producto.getStock() - itemCarrito.getCantidad());
                productoRepository.save(producto);
                
                // Registrar venta para el vendedor del producto
                VentaDetalle ventaDetalle = VentaDetalle.builder()
                    .vendedor(producto.getVendedor())
                    .producto(producto)
                    .nombreProducto(producto.getNombre())
                    .precioUnitario(producto.getPrecio())
                    .cantidad(itemCarrito.getCantidad())
                    .subtotal(producto.getPrecio().multiply(BigDecimal.valueOf(itemCarrito.getCantidad())))
                    .fechaVenta(fechaVenta)
                    .orden(ordenGuardada)
                    .estadoOrden(EstadoOrden.PAGADA)
                    .build();
                
                ventaDetalleRepository.save(ventaDetalle);
            }
            
            // Vaciar el carrito
            carrito.getItems().clear();
            carrito.setTotal(BigDecimal.ZERO);
            carritoRepository.save(carrito);
            
        } else {
            // Pago rechazado o error
            ordenGuardada.setEstado(EstadoOrden.PAGO_RECHAZADO);
            ordenGuardada = ordenRepository.save(ordenGuardada);
            
            if (resultadoPago == SimuladorPago.ResultadoPago.RECHAZADO) {
                throw new PagoRechazadoException("El pago ha sido rechazado. Por favor, intente con otro método de pago.");
            } else {
                throw new PagoRechazadoException("Ha ocurrido un error al procesar el pago. Por favor, intente nuevamente.");
            }
        }
        
        // 9. Convertir a DTO y retornar
        return convertirAOrdenResponseDTO(ordenGuardada);
    }

    // Método eliminado por estar duplicado
    
    private void validarStockDisponible(List<ItemCarrito> items) {
        List<String> productosSinStock = items.stream()
            .filter(item -> item.getProducto().getStock() < item.getCantidad())
            .map(item -> item.getProducto().getNombre() + 
                " (Stock: " + item.getProducto().getStock() + 
                ", Solicitado: " + item.getCantidad() + ")")
            .collect(Collectors.toList());
        
        if (!productosSinStock.isEmpty()) {
            throw new StockInsuficienteException(
                "Stock insuficiente para: " + String.join(", ", productosSinStock)
            );
        }
    }

    private OrdenResponseDTO convertirAOrdenResponseDTO(Orden orden) {
        List<OrdenResponseDTO.ItemOrdenDTO> itemsDTO = orden.getItems().stream()
            .map(item -> OrdenResponseDTO.ItemOrdenDTO.builder()
                .productoId(item.getProducto().getId())
                .nombreProducto(item.getProducto().getNombre())
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .subtotal(item.getSubtotal())
                .build())
            .collect(Collectors.toList());
        
        // Calcular descuento aplicado
        BigDecimal descuentoAplicado = BigDecimal.ZERO;
        if ("PROMO2025".equalsIgnoreCase(orden.getCodigoDescuento())) {
            BigDecimal subtotal = itemsDTO.stream()
                .map(OrdenResponseDTO.ItemOrdenDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            descuentoAplicado = subtotal.multiply(new BigDecimal("0.10"));
        }
        
        return OrdenResponseDTO.builder()
            .id(orden.getId())
            .numeroFactura(orden.getNumeroFactura())
            .fechaCreacion(orden.getFechaCreacion())
            .estado(orden.getEstado().name())
            .subtotal(orden.getSubtotal())
            .total(orden.getTotal())
            .descuentoAplicado(descuentoAplicado)
            .codigoDescuento(orden.getCodigoDescuento())
            .direccionEnvio(orden.getDireccionEnvio())
            .metodoPago(orden.getMetodoPago())
            // Información de pago
            .codigoTransaccion(orden.getCodigoTransaccion())
            .detallesPago(orden.getDetallesPago())
            .fechaPago(orden.getFechaPago())
            // Información de envío
            .notasDeEntrega(orden.getNotasDeEntrega())
            .items(itemsDTO)
            .build();
    }

     public OrdenResponseDTO obtenerOrdenPorIdYUsuario(Long ordenId, Long usuarioId) {
        Orden orden = ordenRepository.findByIdAndUsuarioId(ordenId, usuarioId)
            .orElseThrow(() -> new OrdenNotFoundException(
                "Orden no encontrada con ID: " + ordenId + " para el usuario: " + usuarioId
            ));
        
        return convertirAOrdenResponseDTO(orden);
    }

    public List<OrdenResponseDTO> obtenerOrdenesPorUsuario(Long usuarioId) {
        return ordenRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId).stream()
            .map(this::convertirAOrdenResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    public OrdenConfirmacionDTO obtenerResumenOrden(Long usuarioId, OrdenRequestDTO ordenRequest) {
        // 1. Obtener el carrito del usuario
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
            .orElseThrow(() -> new CarritoNotFoundException("No se encontró un carrito asociado al usuario"));
        
        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }
        
        // 2. Validar stock antes de mostrar resumen
        validarStockDisponible(carrito.getItems());
        
        // 3. Convertir items del carrito a DTO de resumen
        List<OrdenConfirmacionDTO.ItemResumenDTO> itemsResumen = carrito.getItems().stream()
            .map(item -> OrdenConfirmacionDTO.ItemResumenDTO.builder()
                .productoId(item.getProducto().getId())
                .nombre(item.getProducto().getNombre())
                .cantidad(item.getCantidad())
                .precioUnitario(item.getProducto().getPrecio())
                .subtotal(item.getProducto().getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())))
                .build())
            .collect(Collectors.toList());
        
        // 4. Calcular subtotal
        BigDecimal subtotal = itemsResumen.stream()
            .map(OrdenConfirmacionDTO.ItemResumenDTO::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 5. Calcular descuento si hay código
        BigDecimal descuento = BigDecimal.ZERO;
        BigDecimal total = subtotal;
        
        if ("PROMO2025".equalsIgnoreCase(ordenRequest.getCodigoDescuento())) {
            descuento = subtotal.multiply(new BigDecimal("0.10"));
            total = subtotal.subtract(descuento);
        }
        
        // 6. Construir y retornar el DTO de confirmación
        return OrdenConfirmacionDTO.builder()
            .items(itemsResumen)
            .direccionEnvio(ordenRequest.getDireccionEnvio())
            .metodoPago(ordenRequest.getMetodoPago())
            .subtotal(subtotal)
            .descuento(descuento)
            .total(total)
            .codigoDescuento(ordenRequest.getCodigoDescuento())
            .build();
    }

    @Override
    @Transactional
    public OrdenResponseDTO cancelarOrden(Long ordenId, Long usuarioId) {
        // 1. Buscar la orden y verificar que pertenezca al usuario
        Orden orden = ordenRepository.findByIdAndUsuarioId(ordenId, usuarioId)
            .orElseThrow(() -> new OrdenNotFoundException(
                "No se encontró la orden con ID: " + ordenId + " para el usuario: " + usuarioId
            ));
        
        // 2. Verificar si se puede cancelar (solo en ciertos estados)
        if (orden.getEstado() == EstadoOrden.ENVIADA || 
            orden.getEstado() == EstadoOrden.EN_TRANSITO ||
            orden.getEstado() == EstadoOrden.ENTREGADA) {
            throw new IllegalStateException(
                "No se puede cancelar la orden porque ya está en estado: " + orden.getEstado()
            );
        }
        
        // 3. Si la orden ya estaba pagada, restaurar el stock
        if (orden.getEstado() == EstadoOrden.PAGADA || 
            orden.getEstado() == EstadoOrden.PREPARANDO_ENVIO) {
            // Devolver stock a los productos
            for (ItemOrden item : orden.getItems()) {
                Producto producto = item.getProducto();
                producto.setStock(producto.getStock() + item.getCantidad());
                productoRepository.save(producto);
            }
        }
        
        // 4. Actualizar el estado de la orden
        orden.setEstado(EstadoOrden.CANCELADA);
        ordenRepository.save(orden);
        
        // 4.1 Actualizar el estado de los detalles de venta asociados
        List<VentaDetalle> ventasAsociadas = ventaDetalleRepository.findByProductoIdIn(
            orden.getItems().stream()
                .map(item -> item.getProducto().getId())
                .collect(Collectors.toList()));
                
        for (VentaDetalle venta : ventasAsociadas) {
            if (venta.getOrden().getId().equals(orden.getId())) {
                venta.setEstadoOrden(EstadoOrden.CANCELADA);
                ventaDetalleRepository.save(venta);
            }
        }
        
        // 5. Convertir a DTO y retornar
        return convertirAOrdenResponseDTO(orden);
    }
}