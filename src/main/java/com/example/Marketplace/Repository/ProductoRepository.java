package com.example.Marketplace.Repository;

import com.example.Marketplace.Entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Devuelve todos los productos que tengan stock mayor a 0
    List<Producto> findByStockGreaterThan(int stock);
}
