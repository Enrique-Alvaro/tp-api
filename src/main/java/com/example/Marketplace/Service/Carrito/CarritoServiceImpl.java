package com.example.Marketplace.Service.Carrito;

import com.example.Marketplace.Entity.Carrito;
import com.example.Marketplace.Entity.ItemCarrito;
import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Exception.ProductoNotFoundException;
import com.example.Marketplace.Exception.StockInsuficienteException;
import com.example.Marketplace.Exception.UsuarioNotFoundException;
import com.example.Marketplace.Repository.CarritoRepository;
import com.example.Marketplace.Repository.ItemCarritoRepository;
import com.example.Marketplace.Repository.ProductoRepository;
import com.example.Marketplace.Repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;


    @Override
    public Carrito obtenerCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getCarrito() == null) {
            Carrito nuevoCarrito = new Carrito();
            nuevoCarrito.setUsuario(usuario);
            nuevoCarrito.setItems(new ArrayList<>());
            usuario.setCarrito(nuevoCarrito);
            carritoRepository.save(nuevoCarrito);
        }

        return usuario.getCarrito();
    }


    @Transactional
    public Carrito agregarProducto(Long usuarioId, Long productoId, int cantidad) {
        // Validaciones iniciales
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new ProductoNotFoundException(productoId));
        
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException(
                "Stock insuficiente. Disponible: " + producto.getStock()
            );
        }
        
        // Obtener o crear carrito
        Carrito carrito = carritoRepository.findByUsuarioId(usuarioId)
            .orElseGet(() -> {
                Carrito nuevoCarrito = new Carrito();
                nuevoCarrito.setUsuario(usuario);
                return carritoRepository.save(nuevoCarrito);
            });
        
        // Buscar ítem existente
        Optional<ItemCarrito> itemExistente = itemCarritoRepository
            .findByCarritoIdAndProductoId(carrito.getId(), productoId);
        
        ItemCarrito item;
        
        if (itemExistente.isPresent()) {
            item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            item = new ItemCarrito();
            item.setCarrito(carrito);
            item.setProducto(producto);
            item.setCantidad(cantidad);
        }
        
        itemCarritoRepository.save(item);
        
        carrito.setTotal(calcularTotalCarrito(carrito.getId()));
        return carritoRepository.save(carrito);
    }

    @Override
    public boolean eliminarProducto(Long usuarioId, Long productoId) {
        Carrito carrito = obtenerCarrito(usuarioId);
        Optional<ItemCarrito> itemOpt = itemCarritoRepository.findByCarritoIdAndProductoId(carrito.getId(), productoId);
        if (itemOpt.isPresent()) {
            itemCarritoRepository.delete(itemOpt.get());
            carrito.setTotal(calcularTotalCarrito(carrito.getId()));
            carritoRepository.save(carrito);
            return true;
        }
        return false;
    }

    @Override
    public void vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerCarrito(usuarioId);
        itemCarritoRepository.deleteByCarritoId(carrito.getId());
        carrito.setTotal(BigDecimal.ZERO);
        carritoRepository.save(carrito);
    }

    private BigDecimal calcularTotalCarrito(Long carritoId) {
        return itemCarritoRepository.getTotalCarrito(carritoId)
            .orElse(BigDecimal.ZERO);
    }
}
