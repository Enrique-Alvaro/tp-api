package com.example.Marketplace.Controller;

import com.example.Marketplace.Entity.Carrito;
import com.example.Marketplace.Service.Carrito.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    // GET /carrito/{usuarioId} → Obtener el carrito de un usuario
    @GetMapping("/{usuarioId}")
    public ResponseEntity<Carrito> obtenerCarrito(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(usuarioId));
    }

    // POST /carrito/{usuarioId}/agregar → Agregar producto al carrito de un usuario
    @PostMapping("/{usuarioId}/agregar")
    public ResponseEntity<Carrito> agregarProducto(
            @PathVariable Long usuarioId,
            @RequestParam Long productoId,
            @RequestParam int cantidad) {
        return ResponseEntity.ok(carritoService.agregarProducto(usuarioId, productoId, cantidad));
    }

    // DELETE /carrito/{usuarioId}/eliminar/{productoId} → Eliminar producto del carrito de un usuario
    @DeleteMapping("/{usuarioId}/eliminar/{productoId}")
    public ResponseEntity<Carrito> eliminarProducto(
            @PathVariable Long usuarioId, 
            @PathVariable Long productoId) {
        return ResponseEntity.ok(carritoService.eliminarProducto(usuarioId, productoId));
    }

    // DELETE /carrito/{usuarioId}/vaciar → Vaciar el carrito de un usuario
    @DeleteMapping("/{usuarioId}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}
 