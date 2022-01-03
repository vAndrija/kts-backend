package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.admin.AdminCreateDto;
import com.kti.restaurant.dto.admin.AdminDto;
import com.kti.restaurant.dto.admin.AdminUpdateDto;
import com.kti.restaurant.model.Admin;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.service.contract.ISalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AdminMapper {

    private ISalaryService salaryService;
    private SalaryMapper salaryMapper;

    @Autowired
    public AdminMapper(ISalaryService salaryService, SalaryMapper salaryMapper) {
        this.salaryMapper = salaryMapper;
        this.salaryService = salaryService;
    }

    public Admin fromAdminCreateDtoToAdmin(AdminCreateDto adminCreateDto) {
        return new Admin(adminCreateDto.getLastName(), adminCreateDto.getName(),
                adminCreateDto.getPhoneNumber(), adminCreateDto.getEmailAddress(), adminCreateDto.getAccountNumber());
    }

    public AdminDto fromAdminToAdminDto(Admin admin) {
        Salary salary = this.salaryService.findSalaryForDate(LocalDate.now(), admin.getId());
        return new AdminDto(admin.getId(), admin.getName(), admin.getLastName(),
                admin.getPhoneNumber(), admin.getEmailAddress(), admin.getAccountNumber(), this.salaryMapper.fromSalarytoSalaryDto(salary));
    }

    public Admin fromAdminUpdateDtoToAdmin(AdminUpdateDto adminUpdateDto) {
        return new Admin(adminUpdateDto.getName(), adminUpdateDto.getLastName(),
                adminUpdateDto.getAccountNumber(), adminUpdateDto.getPhoneNumber());
    }
}
