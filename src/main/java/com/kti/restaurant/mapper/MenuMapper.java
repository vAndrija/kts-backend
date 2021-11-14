package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.menu.CreateMenuDto;
import com.kti.restaurant.dto.menu.UpdateMenuDto;
import com.kti.restaurant.model.Menu;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {
    public Menu fromCreateMenuDtoToMenu(CreateMenuDto createMenuDto) {
        return new Menu(createMenuDto.getName(), createMenuDto.getStartDuration(), createMenuDto.getEndDuration());
    }

    public Menu fromUpdateMenuDtoToMenu(UpdateMenuDto updateMenuDto) {
        return new Menu(updateMenuDto.getName(), updateMenuDto.getStartDuration(), 
                updateMenuDto.getEndDuration(), updateMenuDto.getId());
    }
}
