
package com.example.Marketplace.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // HTTP 403
public class CarritoNotFoundException extends RuntimeException {
    public CarritoNotFoundException(String message) {
        super(message);
    }
    
}