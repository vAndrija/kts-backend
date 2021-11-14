package com.kti.restaurant.dto.user;

public class UserUpdateDto {

    private String name;

    private String lastName;

    private String phoneNumber;

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

}

