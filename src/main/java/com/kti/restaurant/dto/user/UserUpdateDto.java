package com.kti.restaurant.dto.user;

import javax.validation.constraints.NotEmpty;

public class UserUpdateDto {

    @NotEmpty(message = "name should not be null or empty")
    private String name;

    @NotEmpty(message = "lastName should not be null or empty")
    private String lastName;

    @NotEmpty(message = "phoneNumber should not be null or empty")
    private String phoneNumber;

    @NotEmpty(message = "accountNumber should not be null or empty")
    private String accountNumber;

    public UserUpdateDto(){}

    public UserUpdateDto(String name, String lastName, String phoneNumber,String accountNumber) {
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}

