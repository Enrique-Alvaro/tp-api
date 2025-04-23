package com.example.Marketplace.Service;

import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.DTO.AuthenticationRequestLogin;
import com.example.Marketplace.DTO.AuthenticationResponse;
import com.example.Marketplace.Security.JwtService;
import com.example.Marketplace.Repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class authenticationService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public authenticationService(UsuarioRepository usuarioRepository, JwtService jwtService, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // Método para registrar al usuario
    public AuthenticationResponse register(Usuario usuario) {
        // Encriptamos la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        // Guardamos el usuario en la base de datos
        usuarioRepository.save(usuario);
        
        // Generamos un token JWT
        String token = jwtService.generateToken(usuario);

        return new AuthenticationResponse(token);
    }

    // Método para login de usuario (corrigiendo el tipo de parámetro)
    public AuthenticationResponse login(AuthenticationRequestLogin request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Comprobamos si la contraseña es correcta
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Generamos un token JWT
        String token = jwtService.generateToken(usuario);
        
        return new AuthenticationResponse(token);
    }
}
