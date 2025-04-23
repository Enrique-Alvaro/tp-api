package com.example.Marketplace.DTO;

import com.example.Marketplace.Entity.Rol;

public class AuthenticationRequest {

    private String username;
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private Rol role;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String username, String email, String password, String nombre, String apellido, Rol role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Rol getRole() {
        return role;
    }

    public void setRole(Rol role) {
        this.role = role;
    }
}
