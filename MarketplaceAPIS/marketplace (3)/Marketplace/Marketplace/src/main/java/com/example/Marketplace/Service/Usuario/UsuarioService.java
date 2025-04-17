package com.example.Marketplace.Service.Usuario;

import com.example.Marketplace.Entity.Usuario;
import java.util.List;

public interface UsuarioService {
    List<Usuario> getAll();
    Usuario getById(Long id);
    Usuario create(Usuario usuario);
    Usuario update(Long id, Usuario usuario);
    boolean delete(Long id);
}
