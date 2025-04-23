package com.example.Marketplace.Service.Orden;

import com.example.Marketplace.Entity.Carrito;
import com.example.Marketplace.Entity.Orden;
import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Repository.OrdenRepository;
import com.example.Marketplace.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrdenServiceImpl implements OrdenService {

    private final OrdenRepository ordenRepository;
    private final UsuarioRepository usuarioRepository;

    
    @Override
    public Orden crearOrden(Long usuarioId, Carrito carrito) {
        // Obtener el usuario a partir de su ID
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Calcular el total de la orden
        BigDecimal total = carrito.getItems().stream()
            .map(item -> item.getProducto().getPrecio().multiply(new BigDecimal(item.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Crear la nueva orden, basándonos en el carrito
        Orden nuevaOrden = Orden.builder()
            .usuario(usuario)
            .count((long) carrito.getItems().size()) // Puedes ajustar el cálculo según lo necesites
            .total(total)  // Asignar el total calculado
            .build();

        // Guardar la orden en la base de datos
        return ordenRepository.save(nuevaOrden);
    }


    @Override
    public Orden obtenerOrdenPorId(Long id) {
        // Método para obtener una orden por ID
        return ordenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
    }
}
