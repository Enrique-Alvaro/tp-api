package com.example.Marketplace.Controller;

import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Service.Producto.ProductoServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoServiceImpl productoService;

    // GET /productos → Listar productos con stock
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllConStock());
    }

    // GET /productos/{id} → Detalle del producto
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Producto producto = productoService.getById(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }

    // POST /productos → Crear producto
    // POST /productos → Crear producto (solo VENDEDOR)
    @PostMapping
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<String> createProducto(@RequestBody Producto producto) {
        Producto creado = productoService.create(producto);
        if (creado != null) {
            return ResponseEntity.ok("Producto creado correctamente con ID: " + creado.getId());
        } else {
            return ResponseEntity.badRequest().body("No se pudo crear el producto.");
        }
    }

    // PUT /productos/{id} → Modificar producto (solo VENDEDOR)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<String> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto updated = productoService.update(id, producto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Producto actualizado correctamente con ID: " + updated.getId());
    }

    // DELETE /productos/{id} → Eliminar producto (solo VENDEDOR)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id) {
        boolean eliminado = productoService.delete(id);
        if (!eliminado) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Producto eliminado correctamente con ID: " + id);
    }


}
