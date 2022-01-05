package com.kti.restaurant.dto.user;

import com.kti.restaurant.dto.salary.SalaryDto;
import com.kti.restaurant.model.Role;
import com.kti.restaurant.model.Salary;

public class UserDto {
    private Integer id;

    private String name;

    private String lastName;

    private String phoneNumber;

    private String emailAddress;

    private String accountNumber;

    private String role;

    private SalaryDto salaryDto;

    public UserDto(){}

    public UserDto(String name, String lastName, String phoneNumber, String emailAddress,String accountNumber) {
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.accountNumber = accountNumber;
    }

    public UserDto(Integer id, String name, String lastName, String phoneNumber, String emailAddress,
                   String accountNumber, String role, SalaryDto salaryDto) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.accountNumber = accountNumber;
        this.role = role;
        this.salaryDto = salaryDto;
    }

    public UserDto(Integer id, String name, String lastName, String phoneNumber, String emailAddress,
                   String accountNumber, String role) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.accountNumber = accountNumber;
        this.role = role;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public SalaryDto getSalaryDto() {
        return salaryDto;
    }

    public void setSalaryDto(SalaryDto salaryDto) {
        this.salaryDto = salaryDto;
    }
}

