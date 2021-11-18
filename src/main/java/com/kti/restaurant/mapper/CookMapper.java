package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.cook.CookCreateDto;
import com.kti.restaurant.dto.cook.CookDto;
import com.kti.restaurant.dto.cook.CookUpdateDto;
import com.kti.restaurant.model.Cook;
import org.springframework.stereotype.Component;

@Component
public class CookMapper {
    public Cook fromCookCreateDtoToCook(CookCreateDto cookCreateDto) {
        return new Cook(cookCreateDto.getLastName(),cookCreateDto.getName(),
                cookCreateDto.getPhoneNumber(),cookCreateDto.getEmailAddress(),
                cookCreateDto.getAccountNumber(),cookCreateDto.getPriority(),cookCreateDto.getPassword());
    }

    public CookDto fromCookToCookDto(Cook cook) {
        return new CookDto(cook.getId(),cook.getName(),cook.getLastName(),cook.getPhoneNumber(),
                cook.getEmailAddress(),cook.getAccountNumber(),cook.getPriority());
    }

    public Cook fromCookUpdateDtoToCook(CookUpdateDto cookUpdateDto) {
        return new Cook(cookUpdateDto.getName(),cookUpdateDto.getLastName(),
                cookUpdateDto.getAccountNumber(),cookUpdateDto.getPhoneNumber(),cookUpdateDto.getPriority());
    }
}
