package com.kti.restaurant.dto.admin;

import com.kti.restaurant.dto.user.UserDto;

public class AdminDto extends UserDto {

    public AdminDto() {
        super();
    }

    public AdminDto(Integer id, String name, String lastName, String phoneNumber, String emailAddress,String accountNumber) {
        super(id, name, lastName, phoneNumber, emailAddress,accountNumber);
    }
}
