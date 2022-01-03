package com.kti.restaurant.dto.bartender;

import com.kti.restaurant.dto.salary.SalaryDto;
import com.kti.restaurant.dto.user.UserDto;

public class BartenderDto extends UserDto {
    private Boolean priority;

    public BartenderDto() {
        super();
    }

    public BartenderDto(Integer id, String name, String lastName, String phoneNumber, String emailAddress,
                        String accountNumber, Boolean priority, SalaryDto salaryDto) {
        super(id,name, lastName, phoneNumber, emailAddress, accountNumber,"BARTENDER",salaryDto);
        this.priority = priority;
    }

    public Boolean getPriority() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }
}
