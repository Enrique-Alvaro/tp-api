package com.example.Marketplace.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoResponseDTO {
    private Long id;
    private List<ItemCarritoResponseDTO> items;
    private BigDecimal total;
    private int totalItems;
}