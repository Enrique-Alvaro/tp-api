package com.example.Marketplace.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Configuración para la gestión de sesiones
 * Importa la configuración de cookies para asegurar que se apliquen las propiedades de seguridad
 */
@Configuration
@Import(CookieConfig.class)
public class SessionConfig {
    
    // La configuración de cookies se importa desde CookieConfig.java
    // No es necesario definir beans adicionales aquí ya que CookieConfig ya define el CookieSerializer
    
}
