package com.example.Marketplace.Controller;

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
    
    @PostMapping
    public ResponseEntity<OrdenResponseDTO> crearOrden(
        @AuthenticationPrincipal Usuario usuario,
        @Valid @RequestBody OrdenRequestDTO ordenRequest
    ) {
        OrdenResponseDTO orden = ordenService.crearOrdenDesdeCarrito(usuario.getId(), ordenRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orden);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrdenResponseDTO> obtenerOrden(
        @AuthenticationPrincipal Usuario usuario,
        @PathVariable Long id
    ) {
        OrdenResponseDTO orden = ordenService.obtenerOrdenPorIdYUsuario(id, usuario.getId());
        return ResponseEntity.ok(orden);
    }
    
    @GetMapping
    public ResponseEntity<List<OrdenResponseDTO>> obtenerHistorialOrdenes(
        @AuthenticationPrincipal Usuario usuario
    ) {
        List<OrdenResponseDTO> ordenes = ordenService.obtenerOrdenesPorUsuario(usuario.getId());
        return ResponseEntity.ok(ordenes);
    }
}