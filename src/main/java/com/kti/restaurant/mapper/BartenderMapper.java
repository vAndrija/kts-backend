package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.bartender.BartenderCreateDto;
import com.kti.restaurant.dto.bartender.BartenderDto;
import com.kti.restaurant.dto.bartender.BartenderUpdateDto;
import com.kti.restaurant.model.Bartender;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.service.contract.ISalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BartenderMapper {

    private ISalaryService salaryService;
    private SalaryMapper salaryMapper;

    @Autowired
    public BartenderMapper(ISalaryService salaryService, SalaryMapper salaryMapper) {
        this.salaryMapper = salaryMapper;
        this.salaryService = salaryService;
    }

    public Bartender fromBartenderCreateDtoToBartender(BartenderCreateDto bartenderCreateDto) {
        return new Bartender(bartenderCreateDto.getLastName(), bartenderCreateDto.getName(),
                bartenderCreateDto.getPhoneNumber(), bartenderCreateDto.getEmailAddress(),
                bartenderCreateDto.getAccountNumber(), bartenderCreateDto.getPriority());
    }

    public BartenderDto fromBartenderToBartenderDto(Bartender bartender) {
        Salary salary = this.salaryService.findSalaryForDate(LocalDate.now(), bartender.getId());
        return new BartenderDto(bartender.getId(), bartender.getName(), bartender.getLastName(), bartender.getPhoneNumber(),
                bartender.getEmailAddress(), bartender.getAccountNumber(), bartender.getPriority(), this.salaryMapper.fromSalarytoSalaryDto(salary));
    }

    public Bartender fromBartenderUpdateDtoToBartender(BartenderUpdateDto bartenderUpdateDto) {
        return new Bartender(bartenderUpdateDto.getName(), bartenderUpdateDto.getLastName(),
                bartenderUpdateDto.getAccountNumber(), bartenderUpdateDto.getPhoneNumber(), bartenderUpdateDto.getPriority());
    }
}
