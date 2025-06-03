package com.example.Marketplace.Service.Producto;

import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Entity.Rol;
import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Exception.ProductoNotFoundException;
import com.example.Marketplace.Exception.UsuarioNotFoundException;
import com.example.Marketplace.Repository.ProductoRepository;
import com.example.Marketplace.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    // Obtener todos los productos con stock > 0
    public List<Producto> getAllConStock() {
        return productoRepository.findByStockGreaterThan(0);
    }

    // Obtener producto por ID
    public Producto getById(Long id) {
        return productoRepository.findById(id)
            .orElseThrow(() -> new ProductoNotFoundException(id)); // Lanza excepción personalizada
    }

    // Crear producto
    public Producto create(Producto producto) {
    
        if (producto.getVendedor() == null || producto.getVendedor().getId() == null) {
            throw new IllegalArgumentException("El producto debe tener un usuario asociado");
        }

        Long usuarioId = producto.getVendedor().getId();
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));
        if (usuario.getRole() == Rol.COMPRADOR) {
            usuario.setRole(Rol.VENDEDOR);
            usuarioRepository.save(usuario);
        }
        // if (usuario.getRole() != Rol.VENDEDOR) {
        //     throw new UnauthorizedRoleException("El usuario no tiene permiso para crear productos");
        // }

        if (producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        return productoRepository.save(producto);
    }

    // Actualizar producto
    public Producto update(Long id, Producto producto) {
        Producto existente = productoRepository.findById(id)
            .orElseThrow(() -> new ProductoNotFoundException(id)); // Lanza excepción si no existe

        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        existente.setCategoria(producto.getCategoria());

        return productoRepository.save(existente);
    }

    // Eliminar producto
    public boolean delete(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ProductoNotFoundException(id); // Lanza excepción si no existe
        }
        productoRepository.deleteById(id);
        return true;
    }
}
