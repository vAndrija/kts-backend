package com.kti.restaurant.dto.admin;

import com.kti.restaurant.dto.user.UserUpdateDto;

public class AdminUpdateDto extends UserUpdateDto {

    public AdminUpdateDto() {
        super();
    }

    public AdminUpdateDto(String name, String lastName, String phoneNumber, String accountNumber) {
        super(name, lastName, phoneNumber, accountNumber);
    }
}
