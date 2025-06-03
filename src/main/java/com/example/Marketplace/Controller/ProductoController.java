package com.example.Marketplace.Controller;

import com.example.Marketplace.DTO.ProductoDTO;
import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Entity.Rol;
import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Repository.UsuarioRepository;
import com.example.Marketplace.Service.Producto.ProductoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoServiceImpl productoService;
    private final UsuarioRepository usuarioRepository;

    // GET /productos → Listar productos con stock (acceso para cualquier usuario autenticado)
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoService.getAllConStock());
    }

    // GET /productos/{id} → Detalle del producto (acceso para cualquier usuario autenticado)
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getProductoById(@PathVariable Long id) {
        Producto producto = productoService.getById(id);
        if (producto == null) {
            return ResponseEntity.status(404).body("🔍 Producto no encontrado con ID: " + id);
        }
        return ResponseEntity.ok(producto);
    }

    // POST /productos → Crear producto (solo VENDEDOR)
    @PostMapping
@PreAuthorize("hasRole('VENDEDOR')")
public ResponseEntity<String> createProducto(@RequestBody ProductoDTO dto, Authentication authentication) {
    String username = authentication.getName(); // Ahora usamos username
    System.out.println("Username autenticado: " + username);

    Usuario vendedor = usuarioRepository.findByUsernameAndRole(username, Rol.VENDEDOR)
            .orElseThrow(() -> new IllegalArgumentException("Vendedor no encontrado"));

    Producto creado = productoService.create(dto, vendedor);

    if (creado != null) {
        return ResponseEntity.ok("✅ Producto creado con éxito (ID: " + creado.getId() + ")");
    } else {
        return ResponseEntity.badRequest().body("❌ Error al crear el producto.");
    }
}



    // PUT /productos/{id} → Modificar producto (solo VENDEDOR)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<String> updateProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto updated = productoService.update(id, producto);
        if (updated == null) {
            return ResponseEntity.status(404).body("❌ No se encontró el producto con ID: " + id);
        }
        return ResponseEntity.ok("✏️ Producto actualizado correctamente (ID: " + updated.getId() + ")");
    }

    // DELETE /productos/{id} → Eliminar producto (solo VENDEDOR)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('VENDEDOR')")
    public ResponseEntity<String> deleteProducto(@PathVariable Long id) {
        boolean eliminado = productoService.delete(id);
        if (!eliminado) {
            return ResponseEntity.status(404).body("❌ No se pudo eliminar. Producto no encontrado con ID: " + id);
        }
        return ResponseEntity.ok("🗑️ Producto eliminado correctamente (ID: " + id + ")");
    }
}
