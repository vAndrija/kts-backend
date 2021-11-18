package com.kti.restaurant.dto.admin;

import com.kti.restaurant.dto.user.UserCreateDto;

public class AdminCreateDto extends UserCreateDto {

    public AdminCreateDto(){
        super();
    }

    public AdminCreateDto(String name, String lastName, String phoneNumber, String accountNumber, String emailAddress) {
        super(name, lastName, phoneNumber, accountNumber,emailAddress);
    }
}
