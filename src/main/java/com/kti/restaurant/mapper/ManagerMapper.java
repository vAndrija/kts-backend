package com.kti.restaurant.mapper;


import com.kti.restaurant.dto.manager.ManagerCreateDto;
import com.kti.restaurant.dto.manager.ManagerDto;
import com.kti.restaurant.dto.manager.ManagerUpdateDto;
import com.kti.restaurant.model.Manager;
import org.springframework.stereotype.Component;

@Component
public class ManagerMapper {

    public Manager fromManagerCreateDtoToManger(ManagerCreateDto managerCreateDto) {
        return new Manager(managerCreateDto.getLastName(),managerCreateDto.getName(), managerCreateDto.getPhoneNumber(),
                managerCreateDto.getEmailAddress(),managerCreateDto.getAccountNumber(),managerCreateDto.getPassword());
    }

    public ManagerDto fromManagerToManagerDto(Manager manager) {
        return new ManagerDto(manager.getId(),manager.getName(),manager.getLastName(),
                manager.getPhoneNumber(),manager.getEmailAddress(),manager.getAccountNumber());
    }

    public Manager fromManagerUpdateDtoToManager(ManagerUpdateDto managerUpdateDto) {
        return new Manager(managerUpdateDto.getName(),managerUpdateDto.getLastName(),
                managerUpdateDto.getAccountNumber(),managerUpdateDto.getPhoneNumber());
    }
}
