package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.waiter.WaiterCreateDto;
import com.kti.restaurant.dto.waiter.WaiterDto;
import com.kti.restaurant.dto.waiter.WaiterUpdateDto;
import com.kti.restaurant.model.Waiter;
import org.springframework.stereotype.Component;

@Component
public class WaiterMapper {

    public Waiter fromWaiterCreateDtoToWaiter(WaiterCreateDto waiterCreateDto) {
        return new Waiter(waiterCreateDto.getLastName(),waiterCreateDto.getName(), waiterCreateDto.getPhoneNumber(),
                waiterCreateDto.getEmailAddress(),waiterCreateDto.getAccountNumber(),waiterCreateDto.getPassword());
    }

    public WaiterDto fromWaiterToWaiterDto(Waiter waiter) {
        return new WaiterDto(waiter.getId(),waiter.getName(),waiter.getLastName(),waiter.getPhoneNumber(),waiter.getEmailAddress(),waiter.getAccountNumber());
    }

    public Waiter fromWaiterUpdateDtoToWaiter(WaiterUpdateDto waiterUpdateDto) {
        return new Waiter(waiterUpdateDto.getId(),waiterUpdateDto.getName(),waiterUpdateDto.getLastName(),
                waiterUpdateDto.getAccountNumber(),waiterUpdateDto.getPhoneNumber());
    }
}
