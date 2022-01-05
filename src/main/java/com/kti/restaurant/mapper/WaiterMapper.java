package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.waiter.WaiterCreateDto;
import com.kti.restaurant.dto.waiter.WaiterDto;
import com.kti.restaurant.dto.waiter.WaiterUpdateDto;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.model.Waiter;
import com.kti.restaurant.service.contract.ISalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class WaiterMapper {

    private ISalaryService salaryService;
    private SalaryMapper salaryMapper;

    @Autowired
    public WaiterMapper(ISalaryService salaryService, SalaryMapper salaryMapper) {
        this.salaryMapper = salaryMapper;
        this.salaryService = salaryService;
    }

    public Waiter fromWaiterCreateDtoToWaiter(WaiterCreateDto waiterCreateDto) {
        return new Waiter(waiterCreateDto.getLastName(), waiterCreateDto.getName(), waiterCreateDto.getPhoneNumber(),
                waiterCreateDto.getEmailAddress(), waiterCreateDto.getAccountNumber());
    }

    public WaiterDto fromWaiterToWaiterDto(Waiter waiter) {
        Salary salary = this.salaryService.findSalaryForDate(LocalDate.now(), waiter.getId());
        if(salary!=null)
            return new WaiterDto(waiter.getId(), waiter.getName(), waiter.getLastName(),
                    waiter.getPhoneNumber(), waiter.getEmailAddress(), waiter.getAccountNumber(), salaryMapper.fromSalarytoSalaryDto(salary));
        else
            return new WaiterDto(waiter.getId(), waiter.getName(), waiter.getLastName(),
                    waiter.getPhoneNumber(), waiter.getEmailAddress(), waiter.getAccountNumber());

    }

    public Waiter fromWaiterUpdateDtoToWaiter(WaiterUpdateDto waiterUpdateDto) {
        return new Waiter(waiterUpdateDto.getName(), waiterUpdateDto.getLastName(),
                waiterUpdateDto.getAccountNumber(), waiterUpdateDto.getPhoneNumber());
    }
}
