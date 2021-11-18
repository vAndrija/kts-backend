package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.salary.CreateSalaryDto;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.model.User;
import com.kti.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class SalaryMapper {

    private UserService userService;

    @Autowired
    public SalaryMapper(UserService userService) {
        this.userService = userService;
    }

    public Salary fromCreateSalaryDtoToSalary(CreateSalaryDto salaryDto) {

        return new Salary(salaryDto.getValue(), salaryDto.getStartDate(), salaryDto.getEndDate(),
                (User) userService.loadUserByUsername(salaryDto.getUserEmail()));
    }
}
