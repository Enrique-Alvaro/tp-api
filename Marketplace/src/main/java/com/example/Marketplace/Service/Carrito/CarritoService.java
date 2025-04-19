package com.example.Marketplace.Service.Carrito;

import com.example.Marketplace.Entity.Carrito;

public interface CarritoService {

    Carrito obtenerCarrito();  // Obtiene el carrito global

    Carrito agregarProducto(Long productoId, int cantidad);  // Agrega un producto al carrito global

    Carrito eliminarProducto(Long productoId);  // Elimina un producto del carrito global

    void vaciarCarrito();  // Vacía el carrito global
}
