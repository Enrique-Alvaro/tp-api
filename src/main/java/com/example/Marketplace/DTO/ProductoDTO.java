package com.example.Marketplace.DTO;
import java.math.BigDecimal;
public class ProductoDTO {
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private String categoria; // Puede ser String o Enum, según tu modelo

    // No enviamos vendedorId, lo sacamos del JWT en el controlador

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

}
