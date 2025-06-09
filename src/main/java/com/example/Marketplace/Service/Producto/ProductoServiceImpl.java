package com.example.Marketplace.Service.Producto;

import com.example.Marketplace.DTO.ProductoResponseDTO;
import com.example.Marketplace.Entity.Categoria;
import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Exception.ProductoNotFoundException;
import com.example.Marketplace.Exception.UnauthorizedRoleException;
import com.example.Marketplace.Repository.ProductoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;    // Helper method to check if a product belongs to a vendor
    private boolean isProductoOwnedByVendedor(Producto producto, Usuario vendedor) {
        return producto.getVendedor() != null && 
               vendedor != null && 
               Objects.equals(producto.getVendedor().getId(), vendedor.getId());
    }

    // Obtener todos los productos activos con stock > 0
    public List<ProductoResponseDTO> getAllConStock() {
        List<Producto> productos = productoRepository.findByDeletedFalseAndStockGreaterThan(0);
        return productos.stream()
                .map(ProductoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Obtener productos activos filtrados por nombre y/o categoría con stock > 0
    public List<ProductoResponseDTO> getProductosFiltrados(String nombre, Categoria categoria) {
        List<Producto> productos;
        
        if (nombre != null && categoria != null) {
            // Filtro por nombre y categoría
            productos = productoRepository.findByDeletedFalseAndNombreContainingIgnoreCaseAndCategoriaAndStockGreaterThan(nombre, categoria, 0);
        } else if (nombre != null) {
            // Filtro solo por nombre
            productos = productoRepository.findByDeletedFalseAndNombreContainingIgnoreCaseAndStockGreaterThan(nombre, 0);
        } else if (categoria != null) {
            // Filtro solo por categoría
            productos = productoRepository.findByDeletedFalseAndCategoriaAndStockGreaterThan(categoria, 0);
        } else {
            // Sin filtros, traer todos activos con stock
            productos = productoRepository.findByDeletedFalseAndStockGreaterThan(0);
        }
        
        return productos.stream()
                .map(ProductoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Obtener producto activo por ID
    public ProductoResponseDTO getById(Long id) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new ProductoNotFoundException(id));
        
        if (producto.isDeleted()) {
            throw new ProductoNotFoundException(id);
        }
        
        return ProductoResponseDTO.fromEntity(producto);
    }

    // Crear producto
    public ProductoResponseDTO create(Producto producto) {
        // Validamos que el producto tenga un vendedor asociado
        if (producto.getVendedor() == null) {
            throw new IllegalArgumentException("El producto debe tener un vendedor asociado");
        }
        
        // Validamos el precio
        if (producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        Producto savedProducto = productoRepository.save(producto);
        return ProductoResponseDTO.fromEntity(savedProducto);
    }

    // Este método se ha movido más abajo con un enfoque de reutilización

    // Soft delete de producto
    public boolean delete(Long id) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new ProductoNotFoundException(id));
        producto.setDeleted(true);
        productoRepository.save(producto);
        return true;
    }
    
    // Soft delete de producto con verificación de propietario
    public boolean delete(Long id, Usuario vendedorActual) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new ProductoNotFoundException(id));
        
        // Verificar que el producto pertenezca al vendedor actual
        if (!isProductoOwnedByVendedor(producto, vendedorActual)) {
            throw new UnauthorizedRoleException("No tienes permiso para eliminar este producto");
        }
        
        producto.setDeleted(true);
        productoRepository.save(producto);
        return true;
    }
    
    //Método update con compatibilidad para versiones anteriores
    public ProductoResponseDTO update(Long id, Producto producto) {
        Producto existente = productoRepository.findById(id)
            .orElseThrow(() -> new ProductoNotFoundException(id));

        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        existente.setCategoria(producto.getCategoria());
        if (producto.getImagenes() != null && !producto.getImagenes().isEmpty()) {
            existente.setImagenes(producto.getImagenes());
        }

        Producto savedProducto = productoRepository.save(existente);
        return ProductoResponseDTO.fromEntity(savedProducto);
    }
    
    // Método update con verificación de propietario
    public ProductoResponseDTO update(Long id, Producto producto, Usuario vendedorActual) {
        Producto existente = productoRepository.findById(id)
            .orElseThrow(() -> new ProductoNotFoundException(id));
        
        // Verificar que el producto pertenezca al vendedor actual
        if (!isProductoOwnedByVendedor(existente, vendedorActual)) {
            throw new UnauthorizedRoleException("No tienes permiso para modificar este producto");
        }

        return update(id, producto);
    }
    
    // Obtener productos activos por vendedor
    public List<ProductoResponseDTO> getByVendedor(Long vendedorId) {
        List<Producto> productos = productoRepository.findByDeletedFalseAndVendedorId(vendedorId);
        return productos.stream()
                .map(ProductoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
