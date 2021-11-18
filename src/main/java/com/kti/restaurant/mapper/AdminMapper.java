package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.admin.AdminCreateDto;
import com.kti.restaurant.dto.admin.AdminDto;
import com.kti.restaurant.dto.admin.AdminUpdateDto;
import com.kti.restaurant.model.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public Admin fromAdminCreateDtoToAdmin(AdminCreateDto adminCreateDto) {
        return new Admin(adminCreateDto.getLastName(),adminCreateDto.getName(),
                adminCreateDto.getPhoneNumber(),adminCreateDto.getEmailAddress(),adminCreateDto.getAccountNumber(),adminCreateDto.getPassword());
    }

    public AdminDto fromAdminToAdminDto(Admin admin) {
        return new AdminDto(admin.getId(),admin.getName(),admin.getLastName(),admin.getPhoneNumber(),admin.getEmailAddress(),admin.getAccountNumber());
    }

    public Admin fromAdminUpdateDtoToAdmin(AdminUpdateDto adminUpdateDto) {
        return new Admin(adminUpdateDto.getName(),adminUpdateDto.getLastName(),
                adminUpdateDto.getAccountNumber(),adminUpdateDto.getPhoneNumber());
    }
}
