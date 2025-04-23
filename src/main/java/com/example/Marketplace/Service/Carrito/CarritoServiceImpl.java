package com.example.Marketplace.Service.Carrito;

import com.example.Marketplace.Entity.Carrito;
import com.example.Marketplace.Entity.ItemCarrito;
import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Entity.Usuario;
import com.example.Marketplace.Repository.CarritoRepository;
import com.example.Marketplace.Repository.ProductoRepository;
import com.example.Marketplace.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;

    @Override
    public Carrito obtenerCarrito(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getCarrito() == null) {
            Carrito nuevoCarrito = new Carrito();
            nuevoCarrito.setUsuario(usuario);
            nuevoCarrito.setItems(new ArrayList<>());
            usuario.setCarrito(nuevoCarrito);
            carritoRepository.save(nuevoCarrito);
        }

        return usuario.getCarrito();
    }

    @Override
    public Carrito agregarProducto(Long usuarioId, Long productoId, int cantidad) {
        Carrito carrito = obtenerCarrito(usuarioId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (carrito.getItems() == null) {
            carrito.setItems(new ArrayList<>());
        }

        // Crear y agregar el nuevo ItemCarrito con referencia al carrito
        ItemCarrito item = new ItemCarrito(carrito, producto, cantidad);
        carrito.getItems().add(item);

        carritoRepository.save(carrito);
        return carrito;
    }

    @Override
    public Carrito eliminarProducto(Long usuarioId, Long productoId) {
        Carrito carrito = obtenerCarrito(usuarioId);

        if (carrito.getItems() != null) {
            carrito.getItems().removeIf(item -> item.getProducto().getId().equals(productoId));
        }

        carritoRepository.save(carrito);
        return carrito;
    }

    @Override
    public void vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerCarrito(usuarioId);

        if (carrito.getItems() != null) {
            carrito.getItems().clear();
        }

        carritoRepository.save(carrito);
    }
}
