package com.example.Marketplace.Service.Carrito;

import com.example.Marketplace.Entity.Carrito;
import com.example.Marketplace.Entity.ItemCarrito;
import com.example.Marketplace.Entity.Producto;
import com.example.Marketplace.Repository.ProductoRepository; // Asegúrate de tener este repositorio
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final ProductoRepository productoRepository;  // Repositorio para obtener productos

    // Carrito global único
    private Carrito carrito = new Carrito();

    @Override
    public Carrito obtenerCarrito() {
        return carrito;  // Devuelve el carrito global
    }

    @Override
    public Carrito agregarProducto(Long productoId, int cantidad) {
        // Lógica para agregar un producto al carrito global
        Producto producto = productoRepository.findById(productoId)
                                              .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        ItemCarrito item = new ItemCarrito(producto, cantidad);  // Usar el constructor de ItemCarrito
        carrito.getItems().add(item);
        return carrito;
    }

    @Override
    public Carrito eliminarProducto(Long productoId) {
        carrito.getItems().removeIf(item -> item.getProducto().getId().equals(productoId));
        return carrito;
    }

    @Override
    public void vaciarCarrito() {
        carrito.getItems().clear();  // Vacía el carrito
    }
}
