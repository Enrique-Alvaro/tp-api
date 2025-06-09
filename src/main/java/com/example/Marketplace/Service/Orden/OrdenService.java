package com.example.Marketplace.Service.Orden;

import java.util.List;

import com.example.Marketplace.DTO.OrdenConfirmacionDTO;
import com.example.Marketplace.DTO.OrdenRequestDTO;
import com.example.Marketplace.DTO.OrdenResponseDTO;

public interface OrdenService {

    // Obtener un resumen de la orden para confirmación antes de procesar el pago
    OrdenConfirmacionDTO obtenerResumenOrden(Long usuarioId, OrdenRequestDTO ordenRequest);
    
    // Crear la orden y procesar el pago
    OrdenResponseDTO crearOrdenDesdeCarrito(Long usuarioId, OrdenRequestDTO ordenRequest);
    
    // Obtener información de una orden específica
    OrdenResponseDTO obtenerOrdenPorIdYUsuario(Long ordenId, Long usuarioId);
    
    // Obtener todas las órdenes de un usuario
    List<OrdenResponseDTO> obtenerOrdenesPorUsuario(Long usuarioId);
    
    // Cancelar una orden (si todavía es posible)
    OrdenResponseDTO cancelarOrden(Long ordenId, Long usuarioId);
}
