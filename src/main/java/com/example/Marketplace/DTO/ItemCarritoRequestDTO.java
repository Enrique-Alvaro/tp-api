package com.example.Marketplace.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.Min;

@Data
public class ItemCarritoRequestDTO {
    @NotNull(message = "El ID de producto es obligatorio")
    private Long productoId;
    
    @Min(value = 1, message = "La cantidad mínima es 1")
    private int cantidad = 1;
}