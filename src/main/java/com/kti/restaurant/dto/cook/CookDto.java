package com.kti.restaurant.dto.cook;

import com.kti.restaurant.dto.user.UserDto;

public class CookDto extends UserDto {
    private Boolean priority;

    public CookDto() {
        super();
    }

    public CookDto(Integer id, String name, String lastName, String phoneNumber, String emailAddress,
                        String accountNumber, Boolean priority) {
        super(id,name, lastName, phoneNumber, emailAddress, accountNumber,"COOK");
        this.priority = priority;
    }

    public Boolean getPriority() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }
}
