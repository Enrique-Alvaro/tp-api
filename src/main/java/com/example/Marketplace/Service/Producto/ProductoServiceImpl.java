package com.example.Marketplace.Service.Producto;

import com.example.Marketplace.DTO.ProductoDTO;
import com.example.Marketplace.Entity.Categoria;
import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Entity.Rol;
import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Exception.ProductoNotFoundException;
import com.example.Marketplace.Exception.UsuarioNotFoundException;
import com.example.Marketplace.Repository.ProductoRepository;
import com.example.Marketplace.Repository.UsuarioRepository;

import jakarta.transaction.Transactional;
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

    @Override
    @Transactional
    public Producto create(ProductoDTO dto, Usuario vendedor) {
    if (vendedor == null || vendedor.getId() == null) {
        throw new IllegalArgumentException("El producto debe tener un usuario asociado");
    }

    Usuario usuario = usuarioRepository.findById(vendedor.getId())
        .orElseThrow(() -> new UsuarioNotFoundException(vendedor.getId()));

    if (usuario.getRole() == Rol.COMPRADOR) {
        usuario.setRole(Rol.VENDEDOR); // lo promociona a vendedor
        usuarioRepository.save(usuario);
    }

    // Crear el producto con los datos del DTO
    Producto producto = new Producto();
    producto.setNombre(dto.getNombre());
    producto.setDescripcion(dto.getDescripcion());
    producto.setPrecio(dto.getPrecio());
    producto.setStock(dto.getStock());

    try {
        producto.setCategoria(Categoria.valueOf(dto.getCategoria()));
    } catch (IllegalArgumentException e) {
        producto.setCategoria(Categoria.OTROS); // fallback
    }

    producto.setVendedor(usuario);

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
