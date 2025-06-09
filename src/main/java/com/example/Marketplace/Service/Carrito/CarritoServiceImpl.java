
package com.example.Marketplace.Service.Carrito;

import com.example.Marketplace.DTO.CarritoResponseDTO;
import com.example.Marketplace.DTO.ItemCarritoRequestDTO;
import com.example.Marketplace.DTO.ItemCarritoResponseDTO;
import com.example.Marketplace.Entity.*;
import com.example.Marketplace.Exception.*;
import com.example.Marketplace.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;

    @Override
    @Transactional
    public CarritoResponseDTO obtenerCarritoDTO(Long usuarioId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        
        List<ItemCarritoResponseDTO> itemsDTO = carrito.getItems().stream()
            .map(this::convertirItemADTO)
            .toList();
        
        BigDecimal total = calcularTotalCarrito(carrito.getId());
        carrito.setTotal(total);
        carritoRepository.save(carrito);

        return CarritoResponseDTO.builder()
            .id(carrito.getId())
            .items(itemsDTO)
            .total(total)
            .totalItems(itemsDTO.stream().mapToInt(ItemCarritoResponseDTO::getCantidad).sum())
            .build();
    }

    @Override
    @Transactional
    public CarritoResponseDTO agregarProducto(Long usuarioId, ItemCarritoRequestDTO itemDTO) {
        // Validaciones
        if (itemDTO == null || itemDTO.getProductoId() == null || itemDTO.getCantidad() <= 0) {
            throw new IllegalArgumentException("Datos del ítem inválidos");
        }

        Producto producto = productoRepository.findById(itemDTO.getProductoId())
            .orElseThrow(() -> new ProductoNotFoundException(itemDTO.getProductoId()));

        // Obtener o crear carrito atómicamente
        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        // Manejar ítem existente o nuevo
        Optional<ItemCarrito> itemExistente = itemCarritoRepository
            .findByCarritoIdAndProductoId(carrito.getId(), itemDTO.getProductoId());

        ItemCarrito item;
        
        if (itemExistente.isPresent()) {
            item = itemExistente.get();
            int cantidadActual = item.getCantidad();
            int nuevaCantidad = itemDTO.getCantidad();
            
            // Si queremos reducir la cantidad
            if (nuevaCantidad < cantidadActual) {
                // Devolvemos la diferencia al stock
                int diferencia = cantidadActual - nuevaCantidad;
                producto.setStock(producto.getStock() + diferencia);
                item.setCantidad(nuevaCantidad);
            } else if (nuevaCantidad > cantidadActual) {
                // Queremos aumentar la cantidad, verificamos stock
                int diferencia = nuevaCantidad - cantidadActual;
                if (producto.getStock() < diferencia) {
                    throw new StockInsuficienteException(
                        "Stock insuficiente para " + producto.getNombre() + 
                        ". Disponible: " + producto.getStock()
                    );
                }
                producto.setStock(producto.getStock() - diferencia);
                item.setCantidad(nuevaCantidad);
            } else {
                // La cantidad es la misma, no se hace nada
            }
        } else {
            // Nuevo ítem, verificar stock completo
            if (producto.getStock() < itemDTO.getCantidad()) {
                throw new StockInsuficienteException(
                    "Stock insuficiente para " + producto.getNombre() + 
                    ". Disponible: " + producto.getStock()
                );
            }
            item = new ItemCarrito();
            item.setCarrito(carrito);
            item.setProducto(producto);
            item.setCantidad(itemDTO.getCantidad());
            
            // Actualizar stock del producto
            producto.setStock(producto.getStock() - itemDTO.getCantidad());
        }

        itemCarritoRepository.save(item);
        productoRepository.save(producto);

        // Actualizar total del carrito
        BigDecimal total = calcularTotalCarrito(carrito.getId());
        carrito.setTotal(total);
        carritoRepository.save(carrito);

        return obtenerCarritoDTO(usuarioId);
    }

    @Override
    @Transactional
    public CarritoResponseDTO eliminarProducto(Long usuarioId, Long productoId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        
        itemCarritoRepository.findByCarritoIdAndProductoId(carrito.getId(), productoId)
            .ifPresent(item -> {
                // Devolver stock al producto
                Producto producto = item.getProducto();
                producto.setStock(producto.getStock() + item.getCantidad());
                productoRepository.save(producto);
                
                // Eliminar ítem
                itemCarritoRepository.delete(item);
                
                // Actualizar total
                BigDecimal total = calcularTotalCarrito(carrito.getId());
                carrito.setTotal(total);
                carritoRepository.save(carrito);
            });

        return obtenerCarritoDTO(usuarioId);
    }

    @Override
    @Transactional
    public CarritoResponseDTO vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        
        // Devolver stock de todos los ítems
        carrito.getItems().forEach(item -> {
            Producto producto = item.getProducto();
            producto.setStock(producto.getStock() + item.getCantidad());
            productoRepository.save(producto);
        });
        
        // Eliminar todos los ítems
        itemCarritoRepository.deleteByCarritoId(carrito.getId());
        
        // Actualizar carrito
        carrito.setTotal(BigDecimal.ZERO);
        carritoRepository.save(carrito);
        
        return obtenerCarritoDTO(usuarioId);
    }

    private Carrito obtenerOCrearCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
            .orElseGet(() -> {
                Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
                
                Carrito nuevoCarrito = new Carrito();
                nuevoCarrito.setUsuario(usuario);
                nuevoCarrito.setItems(new ArrayList<>());
                nuevoCarrito.setTotal(BigDecimal.ZERO);
                
                // Establecer relación bidireccional
                usuario.setCarrito(nuevoCarrito);
                
                // Guardar (la relación en usuario se guarda en cascada)
                return carritoRepository.save(nuevoCarrito);
            });
    }

    private ItemCarritoResponseDTO convertirItemADTO(ItemCarrito item) {
        Producto producto = item.getProducto();
        BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));
        
        return ItemCarritoResponseDTO.builder()
            .productoId(producto.getId())
            .nombre(producto.getNombre())
            .imagenUrl(!producto.getImagenes().isEmpty() ? producto.getImagenes().get(0) : null)
            .precioUnitario(producto.getPrecio())
            .cantidad(item.getCantidad())
            .subtotal(subtotal)
            .disponible(producto.getStock() >= item.getCantidad())
            .build();
    }

    private BigDecimal calcularTotalCarrito(Long carritoId) {
        return itemCarritoRepository.getTotalCarrito(carritoId)
            .orElse(BigDecimal.ZERO);
    }
}


// package com.example.Marketplace.Service.Carrito;

// import com.example.Marketplace.Entity.Carrito;
// import com.example.Marketplace.Entity.ItemCarrito;
// import com.example.Marketplace.Entity.Producto;
// import com.example.Marketplace.Entity.Usuario;
// import com.example.Marketplace.Exception.ProductoNotFoundException;
// import com.example.Marketplace.Exception.StockInsuficienteException;
// import com.example.Marketplace.Exception.UsuarioNotFoundException;
// import com.example.Marketplace.Repository.CarritoRepository;
// import com.example.Marketplace.Repository.ItemCarritoRepository;
// import com.example.Marketplace.Repository.ProductoRepository;
// import com.example.Marketplace.Repository.UsuarioRepository;

// import jakarta.transaction.Transactional;
// import lombok.RequiredArgsConstructor;

// import java.util.Optional;
// import org.springframework.stereotype.Service;

// import java.math.BigDecimal;
// import java.util.ArrayList;

// @Service
// @RequiredArgsConstructor
// public class CarritoServiceImpl implements CarritoService {

//     private final ProductoRepository productoRepository;
//     private final UsuarioRepository usuarioRepository;
//     private final CarritoRepository carritoRepository;
//     private final ItemCarritoRepository itemCarritoRepository;


//     @Override
//     public Carrito obtenerCarrito(Long usuarioId) {
//         Usuario usuario = usuarioRepository.findById(usuarioId)
//                 .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

//         if (usuario.getCarrito() == null) {
//             Carrito nuevoCarrito = new Carrito();
//             nuevoCarrito.setUsuario(usuario);
//             nuevoCarrito.setItems(new ArrayList<>());
//             usuario.setCarrito(nuevoCarrito);
//             carritoRepository.save(nuevoCarrito);
//         }

//         return usuario.getCarrito();
//     }


//     @Transactional
//     public Carrito agregarProducto(Long usuarioId, Long productoId, int cantidad) {
//         // Validaciones iniciales
//         Usuario usuario = usuarioRepository.findById(usuarioId)
//             .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        
//         Producto producto = productoRepository.findById(productoId)
//             .orElseThrow(() -> new ProductoNotFoundException(productoId));
        
//         if (producto.getStock() < cantidad) {
//             throw new StockInsuficienteException(
//                 "Stock insuficiente. Disponible: " + producto.getStock()
//             );
//         }
        
//         // Obtener o crear carrito
//         Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
//             .orElseGet(() -> {
//                 Carrito nuevoCarrito = new Carrito();
//                 nuevoCarrito.setUsuario(usuario);
//                 return carritoRepository.save(nuevoCarrito);
//             });
        
//         // Buscar ítem existente
//         Optional<ItemCarrito> itemExistente = itemCarritoRepository
//             .findByCarritoIdAndProductoId(carrito.getId(), productoId);
        
//         ItemCarrito item;
        
//         if (itemExistente.isPresent()) {
//             item = itemExistente.get();
//             item.setCantidad(item.getCantidad() + cantidad);
//         } else {
//             item = new ItemCarrito();
//             item.setCarrito(carrito);
//             item.setProducto(producto);
//             item.setCantidad(cantidad);
//         }
        
//         itemCarritoRepository.save(item);
        
//         carrito.setTotal(calcularTotalCarrito(carrito.getId()));
//         return carritoRepository.save(carrito);
//     }

//     @Override
//     public boolean eliminarProducto(Long usuarioId, Long productoId) {
//         Carrito carrito = obtenerCarrito(usuarioId);
//         Optional<ItemCarrito> itemOpt = itemCarritoRepository.findByCarritoIdAndProductoId(carrito.getId(), productoId);
//         if (itemOpt.isPresent()) {
//             itemCarritoRepository.delete(itemOpt.get());
//             carrito.setTotal(calcularTotalCarrito(carrito.getId()));
//             carritoRepository.save(carrito);
//             return true;
//         }
//         return false;
//     }

//     @Override
//     public void vaciarCarrito(Long usuarioId) {
//         Carrito carrito = obtenerCarrito(usuarioId);
//         itemCarritoRepository.deleteByCarritoId(carrito.getId());
//         carrito.setTotal(BigDecimal.ZERO);
//         carritoRepository.save(carrito);
//     }

//     private BigDecimal calcularTotalCarrito(Long carritoId) {
//         return itemCarritoRepository.getTotalCarrito(carritoId)
//             .orElse(BigDecimal.ZERO);
//     }
// }
