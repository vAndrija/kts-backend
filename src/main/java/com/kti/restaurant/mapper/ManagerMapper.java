package com.kti.restaurant.mapper;


import com.kti.restaurant.dto.manager.ManagerCreateDto;
import com.kti.restaurant.dto.manager.ManagerDto;
import com.kti.restaurant.dto.manager.ManagerUpdateDto;
import com.kti.restaurant.model.Manager;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.service.contract.ISalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ManagerMapper {

    private ISalaryService salaryService;
    private SalaryMapper salaryMapper;

    @Autowired
    public ManagerMapper(ISalaryService salaryService, SalaryMapper salaryMapper) {
        this.salaryMapper = salaryMapper;
        this.salaryService = salaryService;
    }

    public Manager fromManagerCreateDtoToManger(ManagerCreateDto managerCreateDto) {
        return new Manager(managerCreateDto.getLastName(), managerCreateDto.getName(), managerCreateDto.getPhoneNumber(),
                managerCreateDto.getEmailAddress(), managerCreateDto.getAccountNumber());
    }

    public ManagerDto fromManagerToManagerDto(Manager manager) {
        Salary salary = this.salaryService.findSalaryForDate(LocalDate.now(), manager.getId());
        if(salary!=null)
            return new ManagerDto(manager.getId(), manager.getName(), manager.getLastName(),
                    manager.getPhoneNumber(), manager.getEmailAddress(), manager.getAccountNumber(), salaryMapper.fromSalarytoSalaryDto(salary));
        else
            return new ManagerDto(manager.getId(), manager.getName(), manager.getLastName(),
                    manager.getPhoneNumber(), manager.getEmailAddress(), manager.getAccountNumber());

    }

    public Manager fromManagerUpdateDtoToManager(ManagerUpdateDto managerUpdateDto) {
        return new Manager(managerUpdateDto.getName(), managerUpdateDto.getLastName(),
                managerUpdateDto.getAccountNumber(), managerUpdateDto.getPhoneNumber());
    }
}
