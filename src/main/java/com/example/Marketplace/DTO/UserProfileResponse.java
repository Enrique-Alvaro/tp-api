package com.example.Marketplace.DTO;

import com.example.Marketplace.Entity.Rol;

public class UserProfileResponse {
    private String username;
    private Rol role;

    public UserProfileResponse() {
    }

    public UserProfileResponse(String username, Rol role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Rol getRole() {
        return role;
    }

    public void setRole(Rol role) {
        this.role = role;
    }
}
