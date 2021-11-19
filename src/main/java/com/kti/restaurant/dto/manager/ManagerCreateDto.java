package com.kti.restaurant.dto.manager;

import com.kti.restaurant.dto.user.UserCreateDto;

public class ManagerCreateDto extends UserCreateDto {

    public ManagerCreateDto(){
        super();
    }

    public ManagerCreateDto(String name, String lastName, String phoneNumber, String accountNumber,String emailAddress) {
        super(name, lastName, phoneNumber, accountNumber,emailAddress);
    }
}
