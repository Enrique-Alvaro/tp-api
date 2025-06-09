package com.example.Marketplace.Repository;

import com.example.Marketplace.Entity.Categoria;
import com.example.Marketplace.Entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Devuelve todos los productos no eliminados que tengan stock mayor a 0
    List<Producto> findByDeletedFalseAndStockGreaterThan(int stock);
    
    // Filtros por nombre y stock para productos no eliminados
    List<Producto> findByDeletedFalseAndNombreContainingIgnoreCaseAndStockGreaterThan(String nombre, int stock);
    
    // Filtros por categoría y stock para productos no eliminados
    List<Producto> findByDeletedFalseAndCategoriaAndStockGreaterThan(Categoria categoria, int stock);
    
    // Filtros por nombre, categoría y stock para productos no eliminados
    List<Producto> findByDeletedFalseAndNombreContainingIgnoreCaseAndCategoriaAndStockGreaterThan(String nombre, Categoria categoria, int stock);
    
    // Buscar productos no eliminados por vendedor
    List<Producto> findByDeletedFalseAndVendedorId(Long vendedorId);
}
