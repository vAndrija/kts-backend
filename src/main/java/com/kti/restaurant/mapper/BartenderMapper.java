package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.bartender.BartenderCreateDto;
import com.kti.restaurant.dto.bartender.BartenderDto;
import com.kti.restaurant.dto.bartender.BartenderUpdateDto;
import com.kti.restaurant.model.Bartender;
import org.springframework.stereotype.Component;

@Component
public class BartenderMapper {
    public Bartender fromBartenderCreateDtoToBartender(BartenderCreateDto bartenderCreateDto) {
        return new Bartender(bartenderCreateDto.getLastName(),bartenderCreateDto.getName(),
                bartenderCreateDto.getPhoneNumber(),bartenderCreateDto.getEmailAddress(),
                bartenderCreateDto.getAccountNumber(),bartenderCreateDto.getPriority());
    }

    public BartenderDto fromBartenderToBartenderDto(Bartender bartender) {
        return new BartenderDto(bartender.getId(),bartender.getName(),bartender.getLastName(),bartender.getPhoneNumber(),
                bartender.getEmailAddress(),bartender.getAccountNumber(),bartender.getPriority());
    }

    public Bartender fromBartenderUpdateDtoToBartender(BartenderUpdateDto bartenderUpdateDto) {
        return new Bartender(bartenderUpdateDto.getName(),bartenderUpdateDto.getLastName(),
                bartenderUpdateDto.getAccountNumber(),bartenderUpdateDto.getPhoneNumber(),bartenderUpdateDto.getPriority());
    }
}
