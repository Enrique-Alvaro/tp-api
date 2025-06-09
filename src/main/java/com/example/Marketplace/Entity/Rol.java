package com.example.Marketplace.Entity;

public enum Rol {
    COMPRADOR,
    VENDEDOR,
    ADMIN;

     public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
