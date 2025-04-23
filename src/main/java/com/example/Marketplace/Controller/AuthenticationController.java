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
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
        // Lógica para registrar al usuario
        Usuario usuario = usuarioService.create(new Usuario(
            request.getUsername(),
            request.getEmail(),
            request.getPassword(),
            request.getNombre(),
            request.getApellido(),
            request.getRole()  // Asegúrate de tener este campo en tu request
        ));
        return ResponseEntity.ok(authenticationService.register(usuario));
    }

    // Endpoint para login de usuario
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequestLogin request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
