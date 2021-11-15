package com.kti.restaurant.dto.user;

public class UserCreateDto {

    private String name;

    private String lastName;

    private String phoneNumber;

    private String accountNumber;

    private String emailAddress;

    private String password;

    public UserCreateDto(){}

    public UserCreateDto(String name, String lastName, String phoneNumber, String accountNumber,String password, String emailAddress) {
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.accountNumber = accountNumber;
        this.emailAddress = emailAddress;
        this.password = password;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
