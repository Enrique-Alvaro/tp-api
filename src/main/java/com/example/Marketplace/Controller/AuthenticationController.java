package com.example.Marketplace.Controller;

import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Service.Usuario.UsuarioService;
import com.example.Marketplace.Service.authenticationService;
import com.example.Marketplace.DTO.AuthenticationRequest;
import com.example.Marketplace.DTO.AuthenticationRequestLogin;
import com.example.Marketplace.DTO.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
