package com.kti.restaurant.dto.admin;

import com.kti.restaurant.dto.user.UserDto;

public class AdminDto extends UserDto {

    public AdminDto() {
        super();
    }

    public AdminDto(String name, String lastName, String phoneNumber, String emailAddress) {
        super(name, lastName, phoneNumber, emailAddress);
    }
}
