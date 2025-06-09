package com.example.Marketplace.Entity;

public enum EstadoOrden {
    PENDIENTE,          // Orden creada pero no pagada
    PROCESANDO_PAGO,    // Procesando el pago
    PAGO_RECHAZADO,     // El pago fue rechazado
    PAGADA,             // Pago completado con éxito
    PREPARANDO_ENVIO,   // Orden pagada y en preparación
    ENVIADA,            // Orden enviada al cliente
    EN_TRANSITO,        // Orden en camino
    ENTREGADA,          // Orden entregada al cliente
    CANCELADA           // Orden cancelada
}