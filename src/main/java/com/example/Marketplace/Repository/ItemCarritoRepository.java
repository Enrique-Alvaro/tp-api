package com.example.Marketplace.Repository;

import com.example.Marketplace.Entity.ItemCarrito;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    // Buscar un ítem específico en el carrito
    @Query("SELECT i FROM ItemCarrito i WHERE i.carrito.id = :carritoId AND i.producto.id = :productoId")
    Optional<ItemCarrito> findByCarritoIdAndProductoId(Long carritoId, Long productoId);
    
    // Eliminar todos los ítems de un carrito (para vaciar)
    @Modifying
    @Transactional
    @Query("DELETE FROM ItemCarrito i WHERE i.carrito.id = :carritoId")
    void deleteByCarritoId(Long carritoId);
    
    // Contar ítems en el carrito (útil para UI)
    @Query("SELECT COUNT(i) FROM ItemCarrito i WHERE i.carrito.id = :carritoId")
    int countItemsByCarritoId(Long carritoId);

    // Calcular el total del carrito
    @Query("SELECT SUM(i.cantidad * i.producto.precio) FROM ItemCarrito i WHERE i.carrito.id = :carritoId")
    Optional<BigDecimal> getTotalCarrito(Long carritoId);
}