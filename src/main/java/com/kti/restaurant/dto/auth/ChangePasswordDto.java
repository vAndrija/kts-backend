package com.kti.restaurant.dto.auth;

public class ChangePasswordDto {
    private String oldPassword;
    private String password;

    public ChangePasswordDto(){

    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
