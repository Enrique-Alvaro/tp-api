package com.example.Marketplace.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrdenRequestDTO {
    @NotBlank
    private String direccionEnvio;
    
    @NotBlank
    private String metodoPago;  // TARJETA, EFECTIVO, TRANSFERENCIA
    
    // Detalles de pago (opcionales dependiendo del método de pago)
    private String numeroTarjeta;  // Últimos 4 dígitos o simulación
    private String titularTarjeta;
    
    // Información de envío adicional
    private String notasDeEntrega;
    
    // No longer requiring carritoId as it will be obtained from the user
    private String codigoDescuento;
}