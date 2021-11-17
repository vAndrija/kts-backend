package com.kti.restaurant.dto.manager;

import com.kti.restaurant.dto.user.UserUpdateDto;

public class ManagerUpdateDto extends UserUpdateDto {

    public ManagerUpdateDto() {
        super();
    }

    public ManagerUpdateDto(String name, String lastName, String phoneNumber, String accountNumber) {
        super(name, lastName, phoneNumber, accountNumber);
    }
}
