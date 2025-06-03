package com.example.Marketplace.Service.Producto;

import java.util.List;

import com.example.Marketplace.DTO.ProductoDTO;
import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Entity.Usuario;

public interface ProductoService {

    List<Producto> getAllConStock();
    Producto getById(Long id);

    // Cambiado: de Producto create(Producto producto)
    Producto create(ProductoDTO dto, Usuario vendedor);

    Producto update(Long id, Producto producto);
    boolean delete(Long id);
}
