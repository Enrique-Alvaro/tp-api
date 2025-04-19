package com.example.Marketplace.Service.Producto;

import java.util.List;
import com.example.Marketplace.Entity.Producto;

public interface ProductoService {
    List<Producto> getAllConStock();
    Producto getById(Long id);
    Producto create(Producto producto);
    Producto update(Long id, Producto producto);
    boolean delete(Long id);
}
