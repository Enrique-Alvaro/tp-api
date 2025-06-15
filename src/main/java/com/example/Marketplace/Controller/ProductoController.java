package com.example.Marketplace.Controller;

import com.example.Marketplace.DTO.ProductoResponseDTO;
import com.example.Marketplace.Entity.Categoria;
import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Exception.UnauthorizedRoleException;
import com.example.Marketplace.Service.Producto.ProductoServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoServiceImpl productoService;

    // GET /productos → Listar productos con stock, filtrar por nombre y/o categoría
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> getAllProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Categoria categoria) {
        
        return ResponseEntity.ok(productoService.getProductosFiltrados(nombre, categoria));
    }

    // GET /productos/{id} → Detalle del producto
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> getProductoById(@PathVariable Long id) {
        ProductoResponseDTO producto = productoService.getById(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }

    // POST /productos → Crear producto (solo VENDEDOR)
    @PostMapping
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<String> createProducto(@RequestBody Producto producto, @AuthenticationPrincipal Usuario vendedor) {
        // Asignar el vendedor actual como dueño del producto
        producto.setVendedor(vendedor);
        
        ProductoResponseDTO creado = productoService.create(producto);
        if (creado != null) {
            return ResponseEntity.ok("Producto creado correctamente con ID: " + creado.getId());
        } else {
            return ResponseEntity.badRequest().body("No se pudo crear el producto.");
        }
    }

    // PUT /productos/{id} → Modificar producto (solo VENDEDOR y dueño del producto)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<String> updateProducto(@PathVariable Long id, @RequestBody Producto producto, @AuthenticationPrincipal Usuario vendedor) {
        try {
            ProductoResponseDTO updated = productoService.update(id, producto, vendedor);
            if (updated == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok("Producto actualizado correctamente con ID: " + updated.getId());
        } catch (UnauthorizedRoleException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    // DELETE /productos/{id} → Eliminar producto (solo VENDEDOR y dueño del producto)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id, @AuthenticationPrincipal Usuario vendedor) {
        try {
            System.out.println("ENTRO ACAAAAA");
            boolean eliminado = productoService.delete(id, vendedor);
            if (!eliminado) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok("Producto eliminado correctamente con ID: " + id);
        } catch (UnauthorizedRoleException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    // GET /productos/mis-productos → Lista de productos del vendedor actual
    @GetMapping("/mis-productos")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<List<ProductoResponseDTO>> getMisProductos(@AuthenticationPrincipal Usuario vendedor) {
        if (vendedor == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(productoService.getByVendedor(vendedor.getId()));
    }


}
