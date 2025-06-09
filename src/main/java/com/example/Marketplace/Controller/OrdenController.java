package com.example.Marketplace.Controller;

import com.example.Marketplace.DTO.OrdenConfirmacionDTO;
import com.example.Marketplace.DTO.OrdenRequestDTO;
import com.example.Marketplace.DTO.OrdenResponseDTO;
import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Service.Orden.OrdenServiceImpl;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {
    
    private final OrdenServiceImpl ordenService;
    
    // Endpoint para obtener un resumen de la orden antes de confirmar
    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmarOrden(
        @AuthenticationPrincipal Usuario usuario,
        @Valid @RequestBody OrdenRequestDTO ordenRequest
    ) {
        try {
            OrdenConfirmacionDTO confirmacion = ordenService.obtenerResumenOrden(usuario.getId(), ordenRequest);
            return ResponseEntity.ok(confirmacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    // Endpoint para procesar el pago y crear la orden
    @PostMapping
    public ResponseEntity<?> crearOrden(
        @AuthenticationPrincipal Usuario usuario,
        @Valid @RequestBody OrdenRequestDTO ordenRequest
    ) {
        try {
            OrdenResponseDTO orden = ordenService.crearOrdenDesdeCarrito(usuario.getId(), ordenRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(orden);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerOrden(
        @AuthenticationPrincipal Usuario usuario,
        @PathVariable Long id
    ) {
        try {
            OrdenResponseDTO orden = ordenService.obtenerOrdenPorIdYUsuario(id, usuario.getId());
            return ResponseEntity.ok(orden);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> obtenerHistorialOrdenes(
        @AuthenticationPrincipal Usuario usuario
    ) {
        try {
            List<OrdenResponseDTO> ordenes = ordenService.obtenerOrdenesPorUsuario(usuario.getId());
            return ResponseEntity.ok(ordenes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    // Endpoint para cancelar una orden
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarOrden(
        @AuthenticationPrincipal Usuario usuario,
        @PathVariable Long id
    ) {
        try {
            OrdenResponseDTO orden = ordenService.cancelarOrden(id, usuario.getId());
            return ResponseEntity.ok(orden);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}