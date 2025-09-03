package com.example.Marketplace.Controller;

import com.example.Marketplace.DTO.CarritoResponseDTO;
import com.example.Marketplace.DTO.ItemCarritoRequestDTO;
import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Service.Carrito.CarritoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping("/get")
    public ResponseEntity<CarritoResponseDTO> obtenerCarrito(@AuthenticationPrincipal Usuario usuario) { 
        System.out.println("Holaaaa");
        System.out.println(usuario.getId());
        return ResponseEntity.ok(carritoService.obtenerCarritoDTO(usuario.getId()));
    }

    @PostMapping("/items")
    public ResponseEntity<CarritoResponseDTO> agregarItem(
        @AuthenticationPrincipal Usuario usuario,
        @Valid @RequestBody ItemCarritoRequestDTO itemDTO
    ) {
        System.out.println("Holaaaaaaaaaaaaa");
        System.out.println(usuario.getId());
        return ResponseEntity.ok(carritoService.agregarProducto(usuario.getId(), itemDTO));
    }

    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<CarritoResponseDTO> eliminarItem(
        @AuthenticationPrincipal Usuario usuario,
        @PathVariable Long productoId
    ) {
        return ResponseEntity.ok(carritoService.eliminarProducto(usuario.getId(), productoId));
    }

    @DeleteMapping
    public ResponseEntity<CarritoResponseDTO> vaciarCarrito(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(carritoService.vaciarCarrito(usuario.getId()));
    }
}