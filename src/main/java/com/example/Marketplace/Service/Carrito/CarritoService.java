package com.example.Marketplace.Service.Carrito;

import com.example.Marketplace.DTO.CarritoResponseDTO;
import com.example.Marketplace.DTO.ItemCarritoRequestDTO;

public interface CarritoService {

    // Obtener el carrito de un usuario específico
    CarritoResponseDTO obtenerCarritoDTO(Long usuarioId);

    // Agregar un producto al carrito de un usuario
    CarritoResponseDTO agregarProducto(Long usuarioId, ItemCarritoRequestDTO itemDTO);

    // Eliminar un producto del carrito de un usuario
    CarritoResponseDTO eliminarProducto(Long usuarioId, Long productoId);

    // Vaciar el carrito de un usuario
    CarritoResponseDTO vaciarCarrito(Long usuarioId);
}
