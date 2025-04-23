package com.example.Marketplace.Service.Orden;

import com.example.Marketplace.Entity.Carrito;
import com.example.Marketplace.Entity.Orden;

public interface OrdenService {

    // Método para crear una nueva orden a partir del carrito de un usuario
    Orden crearOrden(Long usuarioId, Carrito carrito);
    
    // Otros métodos que puedas necesitar en el futuro (por ejemplo, obtener orden por ID)
    Orden obtenerOrdenPorId(Long id);
}
