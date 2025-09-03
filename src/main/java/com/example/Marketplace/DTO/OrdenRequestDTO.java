package com.example.Marketplace.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrdenRequestDTO {
    @NotBlank
    private String direccionEnvio;
    
    @NotBlank
    private String metodoPago;
    
    @NotNull
    @Positive
    private Long carritoId;

    private String codigoDescuento;
}