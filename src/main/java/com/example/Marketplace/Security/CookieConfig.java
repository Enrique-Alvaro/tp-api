package com.example.Marketplace.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Configuración para las cookies de sesión
 * Establece propiedades de seguridad para las cookies que almacenan el token
 */
@Configuration
public class CookieConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("token");
        serializer.setUseHttpOnlyCookie(true); // ¡Clave para seguridad!
        serializer.setUseSecureCookie(true); // Solo HTTPS
        serializer.setCookiePath("/");
        // La configuración de SameSite puede no estar disponible en todas las versiones
        // Si está disponible, descomentar la siguiente línea:
        // serializer.setSameSite("Strict"); // Protección CSRF
        return serializer;
    }
}
