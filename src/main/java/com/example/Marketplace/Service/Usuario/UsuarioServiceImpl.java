package com.example.Marketplace.Service.Usuario;

import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Exception.UsuarioNotFoundException;
import com.example.Marketplace.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario getById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    @Override
    public Usuario create(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese email.");
        }
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese username.");
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario update(Long id, Usuario usuario) {
        Optional<Usuario> existente = usuarioRepository.findById(id);
        if (existente.isPresent()) {
            Usuario u = existente.get();

            if (!u.getEmail().equals(usuario.getEmail()) && usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new IllegalArgumentException("Ya existe un usuario registrado con ese email.");
            }
            if (!u.getUsername().equals(usuario.getUsername()) && usuarioRepository.existsByUsername(usuario.getUsername())) {
                throw new IllegalArgumentException("Ya existe un usuario registrado con ese username.");
            }

            u.setNombre(usuario.getNombre());
            u.setApellido(usuario.getApellido());
            u.setUsername(usuario.getUsername());
            u.setEmail(usuario.getEmail());
            u.setRole(usuario.getRole());
            return usuarioRepository.save(u);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException(id);
        }
        usuarioRepository.deleteById(id);
        return true;
    }
}
