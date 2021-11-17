package com.kti.restaurant.dto.waiter;

import com.kti.restaurant.dto.user.UserUpdateDto;

public class WaiterUpdateDto extends UserUpdateDto {

    public WaiterUpdateDto() {
        super();
    }

    public WaiterUpdateDto(String name, String lastName, String phoneNumber, String accountNumber) {
        super(name, lastName, phoneNumber, accountNumber);
    }
}
