package com.example.Marketplace.Repository;

import com.example.Marketplace.Entity.ItemCarrito;
import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    List<ItemCarrito> findByCarrito(Carrito carrito);
    Optional<ItemCarrito> findByCarritoAndProducto(Carrito carrito, Producto producto);
}
