package com.example.Marketplace.DTO;

import lombok.Data;

@Data
public class OrdenDTO {
    private Long id;
    private Long count;
    private Long usuarioId;

    public OrdenDTO(Long id, Long count, Long usuarioId) {
        this.id = id;
        this.count = count;
        this.usuarioId = usuarioId;
    }
}
