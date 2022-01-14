package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.admin.AdminUpdateDto;
import com.kti.restaurant.dto.user.UserDto;
import com.kti.restaurant.model.Admin;
import com.kti.restaurant.model.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UserMapper {

    public UserDto fromUserToUserDto(User user) {
        String[] rolesSplited = user.getRoles().get(0).getName().split("_");
        String role = rolesSplited[rolesSplited.length-1].toLowerCase();
        return new UserDto(user.getId(),user.getName(),user.getLastName(),user.getPhoneNumber(),
                user.getEmailAddress(),user.getAccountNumber(),role);
    }
}
