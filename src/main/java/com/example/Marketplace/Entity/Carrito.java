package com.example.Marketplace.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference // Evita la recursividad infinita al serializar Usuario desde Carrito
    @OneToOne
    @JoinColumn(name = "usuario_id") // La columna que referencia al usuario
    private Usuario usuario; // Relación bidireccional con Usuario

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemCarrito> items;
}
