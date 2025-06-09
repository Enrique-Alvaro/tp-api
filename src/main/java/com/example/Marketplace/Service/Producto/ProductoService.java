package com.example.Marketplace.Service.Producto;

import java.util.List;
import com.example.Marketplace.DTO.ProductoResponseDTO;
import com.example.Marketplace.Entity.Categoria;
import com.example.Marketplace.Entity.Producto;

import com.example.Marketplace.Entity.Usuario;

public interface ProductoService {
    List<ProductoResponseDTO> getAllConStock();
    List<ProductoResponseDTO> getProductosFiltrados(String nombre, Categoria categoria);
    List<ProductoResponseDTO> getByVendedor(Long vendedorId);
    ProductoResponseDTO getById(Long id);
    ProductoResponseDTO create(Producto producto);
    ProductoResponseDTO update(Long id, Producto producto);
    ProductoResponseDTO update(Long id, Producto producto, Usuario vendedorActual);
    boolean delete(Long id);
    boolean delete(Long id, Usuario vendedorActual);
}
