package com.example.Marketplace.DTO;

import com.example.Marketplace.Entity.Categoria;
import com.example.Marketplace.Entity.Producto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private Categoria categoria;
    private List<String> imagenes;
    private Long vendedorId;
    private boolean deleted;

    // Método de conversión de Producto a ProductoResponseDTO
    public static ProductoResponseDTO fromEntity(Producto producto) {
        if (producto == null) return null;
        
        // Explicitly set only the fields we want to expose
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setCategoria(producto.getCategoria());
        dto.setImagenes(producto.getImagenes());
        // Incluimos el ID del vendedor para saber quién es el dueño del producto
        if (producto.getVendedor() != null) {
            dto.setVendedorId(producto.getVendedor().getId());
        }
        dto.setDeleted(producto.isDeleted());
        
        return dto;
    }
}
