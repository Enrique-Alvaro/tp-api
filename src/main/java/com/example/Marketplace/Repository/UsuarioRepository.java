package com.example.Marketplace.Repository;

import com.example.Marketplace.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email); // 👈 necesario
    // Podés agregar métodos personalizados si necesitás buscar por username, email, etc.
}
