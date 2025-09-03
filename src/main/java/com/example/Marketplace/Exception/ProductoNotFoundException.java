package com.example.Marketplace.Exception; // Asegúrate de que el paquete coincida con tu estructura

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND) 
public class ProductoNotFoundException extends RuntimeException {

    private final Long productoId; 

    public ProductoNotFoundException(Long productoId) {
        super("Producto con ID " + productoId + " no encontrado"); 
        this.productoId = productoId;
    }

    public Long getProductoId() {
        return productoId;
    }
}