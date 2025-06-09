package com.example.Marketplace.Service.Vendedor;

import com.example.Marketplace.DTO.VentasVendedorDTO;
import com.example.Marketplace.Entity.Usuario;

public interface VentasService {
    VentasVendedorDTO getVentasByVendedor(Usuario vendedor);
    VentasVendedorDTO getVentasByVendedorId(Long vendedorId);
}
