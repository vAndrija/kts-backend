package com.kti.restaurant.dto.cook;

import com.kti.restaurant.dto.user.UserUpdateDto;

public class CookUpdateDto extends UserUpdateDto {
    private Boolean priority;

    public CookUpdateDto() {
        super();
    }

    public CookUpdateDto(String name, String lastName, String phoneNumber, String accountNumber,Boolean priority) {
        super(name, lastName, phoneNumber, accountNumber);
        this.priority = priority;
    }

    public Boolean getPriority() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }
}
