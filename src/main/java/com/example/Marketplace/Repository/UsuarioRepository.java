package com.example.Marketplace.Repository;

import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Entity.Rol; // Asegurate de importar Rol

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Usuario> findByUsernameAndRole(String username, Rol role); // 👈 AGREGÁ ESTA LÍNEA
}

