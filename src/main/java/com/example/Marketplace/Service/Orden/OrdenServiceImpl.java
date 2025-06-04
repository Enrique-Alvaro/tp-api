package com.example.Marketplace.Service.Orden;

import com.example.Marketplace.DTO.OrdenRequestDTO;
import com.example.Marketplace.DTO.OrdenResponseDTO;
import com.example.Marketplace.Entity.Carrito;
import com.example.Marketplace.Entity.EstadoOrden;
import com.example.Marketplace.Entity.ItemCarrito;
import com.example.Marketplace.Entity.ItemOrden;
import com.example.Marketplace.Entity.Orden;
import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Exception.CarritoNotFoundException;
import com.example.Marketplace.Exception.OrdenNotFoundException;
import com.example.Marketplace.Exception.StockInsuficienteException;
import com.example.Marketplace.Repository.CarritoRepository;
import com.example.Marketplace.Repository.OrdenRepository;
import com.example.Marketplace.Repository.ProductoRepository;
import com.example.Marketplace.Repository.UsuarioRepository;

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
public class OrdenServiceImpl {
    
    private final CarritoRepository carritoRepository;
    private final OrdenRepository ordenRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
        
    @Transactional
    public OrdenResponseDTO crearOrdenDesdeCarrito(Long usuarioId, OrdenRequestDTO ordenRequest) {
        // 1. Obtener el carrito
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
            .orElseThrow(() -> new CarritoNotFoundException("Carrito no encontrado"));
        
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
        BigDecimal total = calcularTotalConDescuento(subtotal, ordenRequest.getCodigoDescuento());
        
        // 5. Crear la orden
        Orden orden = new Orden();
        orden.setUsuario(usuarioRepository.findById(usuarioId).orElseThrow());
        orden.setDireccionEnvio(ordenRequest.getDireccionEnvio());
        orden.setMetodoPago(ordenRequest.getMetodoPago());
        orden.setFechaCreacion(LocalDateTime.now());
        orden.setEstado(EstadoOrden.PENDIENTE);
        orden.setCodigoDescuento(ordenRequest.getCodigoDescuento());
        orden.setSubtotal(subtotal); // <-- ESTABLECER EL SUBTOTAL
        orden.setTotal(total);       // <-- ESTABLECER EL TOTAL
        
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
            
            // Actualizar stock del producto
            Producto producto = itemCarrito.getProducto();
            producto.setStock(producto.getStock() - itemCarrito.getCantidad());
            productoRepository.save(producto);
        }
        
        // 7. Guardar la orden
        Orden ordenGuardada = ordenRepository.save(orden);
        
        // 8. Vaciar el carrito
        carrito.getItems().clear();
        carrito.setTotal(BigDecimal.ZERO);
        carritoRepository.save(carrito);
        
        // 9. Convertir a DTO y retornar
        return convertirAOrdenResponseDTO(ordenGuardada);
    }

    private BigDecimal calcularTotalConDescuento(BigDecimal subtotal, String codigoDescuento) {
        if ("PROMO2025".equalsIgnoreCase(codigoDescuento)) {
            return subtotal.multiply(new BigDecimal("0.90")); // Aplica 10% de descuento
        }
        return subtotal;
    }
    
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
            .subtotal(orden.getTotal().add(descuentoAplicado)) // Mostrar subtotal sin descuento
            .total(orden.getTotal()) // Total con descuento aplicado
            .codigoDescuento(orden.getCodigoDescuento())
            .direccionEnvio(orden.getDireccionEnvio())
            .metodoPago(orden.getMetodoPago())
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
}