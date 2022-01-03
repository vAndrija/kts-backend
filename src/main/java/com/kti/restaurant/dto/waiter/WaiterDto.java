package com.kti.restaurant.dto.waiter;

import com.kti.restaurant.dto.salary.SalaryDto;
import com.kti.restaurant.dto.user.UserDto;

public class WaiterDto extends UserDto {

    public WaiterDto() {
        super();
    }

    public WaiterDto(Integer id, String name, String lastName,
                     String phoneNumber, String emailAddress, String accountNumber, SalaryDto salaryDto) {
        super(id, name, lastName, phoneNumber, emailAddress,accountNumber,"WAITER",salaryDto);
    }
}
