package com.example.Marketplace.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Marketplace.Entity.ItemOrden;
import com.example.Marketplace.Entity.EstadoOrden;

@Repository
public interface ItemOrdenRepository extends JpaRepository<ItemOrden, Long> {
    
    // Encontrar items de orden por lista de IDs de productos
    List<ItemOrden> findByProductoIdIn(List<Long> productosIds);
    
    // Encontrar items de orden por ID de producto
    List<ItemOrden> findByProductoId(Long productoId);
    
    // Encontrar items de orden por ID de producto y estado
    List<ItemOrden> findByProductoIdAndOrden_Estado(Long productoId, EstadoOrden estado);
}
