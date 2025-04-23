package com.example.Marketplace.Controller;

import com.example.Marketplace.Entity.Carrito;
import com.example.Marketplace.Entity.Orden;
import com.example.Marketplace.Service.Orden.OrdenService;
import com.example.Marketplace.Service.Carrito.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;
    private final CarritoService carritoService;

    // POST /ordenes → Crear una nueva orden y vaciar el carrito
    @PostMapping
    public ResponseEntity<Orden> crearOrden(@RequestParam Long usuarioId) {
        // Primero obtenemos el carrito del usuario
        Carrito carrito = carritoService.obtenerCarrito(usuarioId);

        if (carrito.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Si el carrito está vacío
        }

        // Creamos una nueva orden a partir del carrito
        Orden nuevaOrden = ordenService.crearOrden(usuarioId, carrito);

        // Vaciar el carrito después de la compra
        carritoService.vaciarCarrito(usuarioId);

        return ResponseEntity.ok(nuevaOrden);
    }
}
