package com.example.Marketplace.Service.Carrito;

import com.example.Marketplace.Entity.Carrito;

public interface CarritoService {

    // Obtener el carrito de un usuario específico
    Carrito obtenerCarrito(Long usuarioId);

    // Agregar un producto al carrito de un usuario
    Carrito agregarProducto(Long usuarioId, Long productoId, int cantidad);

    // Eliminar un producto del carrito de un usuario
    Carrito eliminarProducto(Long usuarioId, Long productoId);

    // Vaciar el carrito de un usuario
    void vaciarCarrito(Long usuarioId);
}
