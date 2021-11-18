package com.kti.restaurant.dto.cook;

import com.kti.restaurant.dto.user.UserCreateDto;

public class CookCreateDto extends UserCreateDto {
    private Boolean priority;

    public CookCreateDto(){
        super();
    }

    public CookCreateDto(String name, String lastName, String phoneNumber, String accountNumber,
                              String emailAddress, Boolean priority) {
        super(name, lastName, phoneNumber, accountNumber,emailAddress);
        this.priority = priority;
    }

    public Boolean getPriority() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }
}
