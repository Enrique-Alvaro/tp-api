package com.example.Marketplace.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(Long usuarioId) {
        super("Usuario con ID " + usuarioId + " no encontrado"); 
    }

}