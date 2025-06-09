package com.example.Marketplace.Controller;

import com.example.Marketplace.DTO.VentasVendedorDTO;
import com.example.Marketplace.Entity.Rol;
import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Service.Vendedor.VentasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentasController {

    private final VentasService ventasService;

    /**
     * Obtiene las estadísticas de ventas del vendedor autenticado
     * @param usuario El usuario autenticado (debe ser un vendedor)
     * @return DTO con estadísticas de ventas
     */
    @GetMapping("/mis-ventas")
    public ResponseEntity<VentasVendedorDTO> getMisVentas(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }
        
        if (usuario.getRole() != Rol.VENDEDOR) {
            return ResponseEntity.status(403).build();
        }
        System.out.println("CHAU");
        
        VentasVendedorDTO ventasDTO = ventasService.getVentasByVendedor(usuario);
        return ResponseEntity.ok(ventasDTO);
    }
    
    /**
     * Endpoint administrativo para obtener ventas de un vendedor específico por su ID
     * @param vendedorId ID del vendedor
     * @param usuario Usuario autenticado (debe ser ADMIN)
     * @return DTO con estadísticas de ventas del vendedor
     */
    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<VentasVendedorDTO> getVentasByVendedorId(
            @PathVariable Long vendedorId,
            @AuthenticationPrincipal Usuario usuario) {
        
        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }
        
        // Verificar si el usuario es admin o es el propio vendedor consultando sus ventas
        if (usuario.getRole() != Rol.ADMIN && 
            (usuario.getRole() != Rol.VENDEDOR || !usuario.getId().equals(vendedorId))) {
            return ResponseEntity.status(403).build();
        }
        
        VentasVendedorDTO ventasDTO = ventasService.getVentasByVendedorId(vendedorId);
        return ResponseEntity.ok(ventasDTO);
    }
}
