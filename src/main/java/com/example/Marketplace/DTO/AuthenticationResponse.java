package com.example.Marketplace.DTO;

public class AuthenticationResponse {

    private String token;

    // Constructor, getter y setter
    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

