package com.example.Marketplace.Repository;

import com.example.Marketplace.Entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    Optional<Orden> findByIdAndUsuarioId(Long id, Long usuarioId);

    List<Orden> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);
}
