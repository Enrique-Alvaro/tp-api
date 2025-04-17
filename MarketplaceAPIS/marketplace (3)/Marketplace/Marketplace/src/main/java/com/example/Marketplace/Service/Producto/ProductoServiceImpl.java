package com.example.Marketplace.Service.Producto;

import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    // Obtener todos los productos con stock > 0
    public List<Producto> getAllConStock() {
        return productoRepository.findByStockGreaterThan(0);
    }

    // Obtener producto por ID
    public Producto getById(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    // Crear producto
    public Producto create(Producto producto) {
        return productoRepository.save(producto);
    }

    // Actualizar producto
    public Producto update(Long id, Producto producto) {
        Optional<Producto> optional = productoRepository.findById(id);
        if (optional.isEmpty()) return null;

        Producto existente = optional.get();
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
            return false;
        }
        productoRepository.deleteById(id);
        return true;
    }
}
