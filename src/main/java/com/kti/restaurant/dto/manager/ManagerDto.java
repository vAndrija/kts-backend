package com.kti.restaurant.dto.manager;

import com.kti.restaurant.dto.user.UserDto;

public class ManagerDto extends UserDto {

    public ManagerDto() {
        super();
    }

    public ManagerDto(Integer id, String name, String lastName, String phoneNumber, String emailAddress,String accountNumber) {
        super(id, name, lastName, phoneNumber, emailAddress,accountNumber);
    }
}
