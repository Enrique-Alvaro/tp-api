package com.example.Marketplace.Service.Pago;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Simulador de pagos para fines educativos.
 * En un entorno real, esto se integraría con una pasarela de pago.
 */
@Component
@Slf4j
public class SimuladorPago {

    public enum ResultadoPago {
        APROBADO,
        RECHAZADO,
        ERROR_PROCESAMIENTO
    }
    
    /**
     * Simula el procesamiento de un pago
     * @param metodoPago Método de pago (TARJETA, EFECTIVO, TRANSFERENCIA)
     * @param monto Monto a pagar
     * @param detalles Detalles adicionales del pago
     * @return Resultado del pago simulado
     */
    public ResultadoPago procesarPago(String metodoPago, BigDecimal monto, String detalles) {
        // Simulamos una pequeña demora
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        log.info("Procesando pago de {} con método: {}, detalles: {}", monto, metodoPago, detalles);
        
        // Simulación de resultados basados en el monto y el método de pago
        if (monto.compareTo(new BigDecimal("10000")) > 0) {
            log.warn("Pago rechazado: monto demasiado alto");
            return ResultadoPago.RECHAZADO;
        }
        
        // Simulamos un pequeño porcentaje de errores aleatorios
        if (Math.random() < 0.05) {
            log.error("Error en procesamiento de pago");
            return ResultadoPago.ERROR_PROCESAMIENTO;
        }
        
        log.info("Pago aprobado");
        return ResultadoPago.APROBADO;
    }
    
    /**
     * Genera un código de transacción único
     * @return Código de transacción
     */
    public String generarCodigoTransaccion() {
        return "TRX-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }
}
