package com.example.Marketplace.Service.Orden;

import java.util.List;

import com.example.Marketplace.DTO.OrdenRequestDTO;
import com.example.Marketplace.DTO.OrdenResponseDTO;

public interface OrdenService {

    OrdenResponseDTO crearOrdenDesdeCarrito(Long usuarioId, OrdenRequestDTO ordenRequest);
    OrdenResponseDTO obtenerOrdenPorIdYUsuario(Long ordenId, Long usuarioId);
    List<OrdenResponseDTO> obtenerOrdenesPorUsuario(Long usuarioId);
}
