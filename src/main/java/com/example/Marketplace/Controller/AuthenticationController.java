package com.example.Marketplace.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;

import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Service.Usuario.UsuarioService;
import com.example.Marketplace.Service.authenticationService;
import com.example.Marketplace.DTO.AuthenticationRequest;
import com.example.Marketplace.DTO.AuthenticationRequestLogin;
import com.example.Marketplace.DTO.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final authenticationService authenticationService;
    private final UsuarioService usuarioService;

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody AuthenticationRequest request) {
    try {
        Usuario usuario = usuarioService.create(new Usuario(
            request.getUsername(),
            request.getEmail(),
            request.getPassword(),
            request.getNombre(),
            request.getApellido(),
            request.getRole()
        ));
        return ResponseEntity.ok(authenticationService.register(usuario));
    } catch (DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body("El email o nombre de usuario ya existen.");
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("No se pudo registrar el usuario. Por favor, verifica los datos ingresados o intenta más tarde.");
    }
}


    // Endpoint para login de usuario
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestLogin request) {
        try {
            AuthenticationResponse response = authenticationService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciales inválidas o error de autenticación.");
        }
    }

@PostMapping("/logout")
public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession(false);
    if (session != null) {
        session.invalidate();
    }

    Cookie cookie = new Cookie("token", null);
    cookie.setHttpOnly(true);
    cookie.setSecure(false);  // Cambiar a true si usas HTTPS en producción
    cookie.setPath("/");      // Igual que cuando la seteaste al crearla
    cookie.setMaxAge(0);      // Expira inmediatamente para eliminarla
    response.addCookie(cookie);

    return ResponseEntity.ok().body("Logout exitoso");
}
}
