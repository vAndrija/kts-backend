package com.kti.restaurant.dto.bartender;

import com.kti.restaurant.dto.user.UserUpdateDto;

public class BartenderUpdateDto extends UserUpdateDto {
    private Boolean priority;

    public BartenderUpdateDto() {
        super();
    }

    public BartenderUpdateDto(String name, String lastName, String phoneNumber, String accountNumber,Boolean priority) {
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
