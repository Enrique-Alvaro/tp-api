package com.example.Marketplace.Entity;

public enum Rol {
    COMPRADOR,
    VENDEDOR;

     public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
