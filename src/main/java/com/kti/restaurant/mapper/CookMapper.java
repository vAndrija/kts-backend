package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.cook.CookCreateDto;
import com.kti.restaurant.dto.cook.CookDto;
import com.kti.restaurant.dto.cook.CookUpdateDto;
import com.kti.restaurant.model.Cook;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.service.contract.ISalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CookMapper {

    private ISalaryService salaryService;
    private SalaryMapper salaryMapper;

    @Autowired
    public CookMapper(ISalaryService salaryService, SalaryMapper salaryMapper) {
        this.salaryMapper = salaryMapper;
        this.salaryService = salaryService;
    }

    public Cook fromCookCreateDtoToCook(CookCreateDto cookCreateDto) {
        return new Cook(cookCreateDto.getLastName(), cookCreateDto.getName(),
                cookCreateDto.getPhoneNumber(), cookCreateDto.getEmailAddress(),
                cookCreateDto.getAccountNumber(), cookCreateDto.getPriority());
    }

    public CookDto fromCookToCookDto(Cook cook) {
        Salary salary = this.salaryService.findSalaryForDate(LocalDate.now(), cook.getId());
        if (salary!=null)
            return new CookDto(cook.getId(), cook.getName(), cook.getLastName(), cook.getPhoneNumber(),
                cook.getEmailAddress(), cook.getAccountNumber(), cook.getPriority(), salaryMapper.fromSalarytoSalaryDto(salary));
        else
            return new CookDto(cook.getId(), cook.getName(), cook.getLastName(), cook.getPhoneNumber(),
                    cook.getEmailAddress(), cook.getAccountNumber(), cook.getPriority());
    }

    public Cook fromCookUpdateDtoToCook(CookUpdateDto cookUpdateDto) {
        return new Cook(cookUpdateDto.getName(), cookUpdateDto.getLastName(),
                cookUpdateDto.getAccountNumber(), cookUpdateDto.getPhoneNumber(), cookUpdateDto.getPriority());
    }
}
