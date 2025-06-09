package com.example.Marketplace.Service;

import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.DTO.AuthenticationRequestLogin;
import com.example.Marketplace.DTO.AuthenticationResponse;
import com.example.Marketplace.Security.JwtService;
import com.example.Marketplace.Repository.UsuarioRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class authenticationService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final HttpServletResponse response;

    public authenticationService(UsuarioRepository usuarioRepository, JwtService jwtService, BCryptPasswordEncoder passwordEncoder, HttpServletResponse response) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.response = response;
    }

    // Método para registrar al usuario
    public AuthenticationResponse register(Usuario usuario) {
        // Encriptamos la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        // Guardamos el usuario en la base de datos
        usuarioRepository.save(usuario);
        
        // Generamos un token JWT
        String token = jwtService.generateToken(usuario);
        
        // Almacenamos el token en una cookie
        addTokenCookie(token);

        return new AuthenticationResponse(token, usuario.getUsername(), usuario.getRole());
    }

    // Método para login de usuario (corrigiendo el tipo de parámetro)
    public AuthenticationResponse login(AuthenticationRequestLogin request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(usuario);
        
        // Almacenamos el token en una cookie
        addTokenCookie(token);

        return new AuthenticationResponse(token, usuario.getUsername(), usuario.getRole());
    }
    
    // Método privado para añadir el token como cookie
    private void addTokenCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Solo para HTTPS
        cookie.setMaxAge(86400); // 1 día en segundos
        response.addCookie(cookie);
    }
}
