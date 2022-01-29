package com.kti.restaurant.dto.auth;

public class ResetPasswordDTO {
    private String token;
    private String password;
    private String email;

    public ResetPasswordDTO() {
    }

    public ResetPasswordDTO(String token, String password, String email) {
        this.token = token;
        this.password = password;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}