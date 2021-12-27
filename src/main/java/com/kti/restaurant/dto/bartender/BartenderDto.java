package com.kti.restaurant.dto.bartender;

import com.kti.restaurant.dto.user.UserDto;

public class BartenderDto extends UserDto {
    private Boolean priority;

    public BartenderDto() {
        super();
    }

    public BartenderDto(Integer id,String name, String lastName, String phoneNumber, String emailAddress,
                        String accountNumber, Boolean priority) {
        super(id,name, lastName, phoneNumber, emailAddress, accountNumber,"BARTENDER");
        this.priority = priority;
    }

    public Boolean getPriority() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }
}
