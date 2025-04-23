package com.example.Marketplace.DTO;

public class AuthenticationRequestLogin {

    private String email;
    private String password;

    // Constructor, Getters y Setters

    public AuthenticationRequestLogin() {
    }

    public AuthenticationRequestLogin(String email, String password) {
        this.email = email;
        this.password = password;
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
}
