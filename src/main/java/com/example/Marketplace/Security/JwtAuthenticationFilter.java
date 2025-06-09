package com.example.Marketplace.Security;

import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository; // Necesario para cargar tu entidad Usuario

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Intentar extraer el token de la cookie primero
            String jwt = null;
            if (request.getCookies() != null) {
                for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                    if ("token".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }
            
            // Si no hay token en la cookie, intentar extraerlo del header (para compatibilidad)
            if (jwt == null) {
                final String authHeader = request.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    jwt = authHeader.substring(7);
                }
            }
            
            // Si no hay token, continuar con la cadena de filtros
            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }
            final String username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Carga tu entidad Usuario real desde la base de datos
                System.out.println("Email extraído del JWT: " + username);
                Usuario usuario = usuarioRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                System.out.println("Usuario encontrado: " + usuario.getUsername());
                if (jwtService.isTokenValid(jwt, usuario)) {
                    // Crea la autenticación con tu entidad Usuario
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            usuario, // <- Aquí pasas tu entidad Usuario directamente
                            null,
                            usuario.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized: " + e.getMessage() + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}

// package com.example.Marketplace.Security;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import java.io.IOException;

// @Component
// @RequiredArgsConstructor
// public class JwtAuthenticationFilter extends OncePerRequestFilter {

//     private final JwtService jwtService;

//     @Override
//     protected void doFilterInternal(HttpServletRequest request,
//                                     HttpServletResponse response,
//                                     FilterChain filterChain)
//             throws ServletException, IOException {

//         try {
//             final String authHeader = request.getHeader("Authorization");

//             if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                 final String jwt = authHeader.substring(7);
//                 final String email = jwtService.extractUsername(jwt);
//                 final String role = jwtService.extractRole(jwt);

//                 if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//                     // Creamos UserDetails con el rol que viene del token
//                     UserDetails userDetails = User.builder()
//                             .username(email)
//                             .password("") // No se usa aquí
//                             .roles(role)
//                             .build();
//                     System.out.println("🔐 JWT Role: " + role);
//                     System.out.println("🔐 Spring Authority: " + userDetails.getAuthorities());

//                     if (jwtService.isTokenValid(jwt, userDetails)) {
//                         UsernamePasswordAuthenticationToken authToken =
//                                 new UsernamePasswordAuthenticationToken(
//                                         userDetails,
//                                         null,
//                                         userDetails.getAuthorities()
//                                 );
//                         authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                         SecurityContextHolder.getContext().setAuthentication(authToken);
//                     }
//                 }
//             }
//         } catch (Exception e) {
//             System.out.println("Error en el filtro JWT: " + e.getMessage());
//             response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//             response.setContentType("application/json");
//             response.getWriter().write("{\"error\": \"Unauthorized: " + e.getMessage() + "\"}");
//             return;
//         }

//         filterChain.doFilter(request, response);
//     }
    
// }
