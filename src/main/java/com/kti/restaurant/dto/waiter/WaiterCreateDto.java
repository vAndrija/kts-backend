package com.kti.restaurant.dto.waiter;

import com.kti.restaurant.dto.user.UserCreateDto;

public class WaiterCreateDto extends UserCreateDto {

    public WaiterCreateDto(){
        super();
    }

    public WaiterCreateDto(String name, String lastName, String phoneNumber, String accountNumber,String password, String emailAddress) {
        super(name, lastName, phoneNumber, accountNumber,password,emailAddress);
    }
}
