package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.salary.CreateSalaryDto;
import com.kti.restaurant.dto.salary.SalaryDto;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.model.User;
import com.kti.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SalaryMapper {

    private UserService userService;

    @Autowired
    public SalaryMapper(UserService userService) {
        this.userService = userService;
    }

    public Salary fromCreateSalaryDtoToSalary(CreateSalaryDto salaryDto) {
    	User user = userService.findUserByUsername(salaryDto.getUserEmail());
        return new Salary(salaryDto.getValue(), salaryDto.getStartDate(), salaryDto.getEndDate(), user);
    }
    
    public SalaryDto fromSalarytoSalaryDto(Salary salary) {
    	return new SalaryDto(salary.getId(), salary.getValue(), salary.getStartDate(), salary.getEndDate(), 
    			salary.getUser().getName(), salary.getUser().getLastName(), salary.getUser().getEmailAddress());
    }
}
