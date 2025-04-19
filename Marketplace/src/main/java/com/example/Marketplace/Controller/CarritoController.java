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

    // GET /carrito → Obtener el carrito global
    @GetMapping
    public ResponseEntity<Carrito> obtenerCarrito() {
        return ResponseEntity.ok(carritoService.obtenerCarrito());
    }

    // POST /carrito/agregar → Agregar producto al carrito global
    @PostMapping("/agregar")
    public ResponseEntity<Carrito> agregarProducto(
            @RequestParam Long productoId,
            @RequestParam int cantidad) {
        return ResponseEntity.ok(carritoService.agregarProducto(productoId, cantidad));
    }

    // DELETE /carrito/eliminar/{productoId} → Eliminar producto del carrito global
    @DeleteMapping("/eliminar/{productoId}")
    public ResponseEntity<Carrito> eliminarProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(carritoService.eliminarProducto(productoId));
    }

    // DELETE /carrito/vaciar → Vaciar el carrito global
    @DeleteMapping("/vaciar")
    public ResponseEntity<Void> vaciarCarrito() {
        carritoService.vaciarCarrito();
        return ResponseEntity.noContent().build();
    }
}
