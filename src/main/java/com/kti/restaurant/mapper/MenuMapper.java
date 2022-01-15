package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.menu.MenuDto;
import com.kti.restaurant.model.Menu;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {
    public Menu fromMenuDtoToMenu(MenuDto menuDto) {
        return new Menu(menuDto.getName(), menuDto.getStartDuration(), menuDto.getEndDuration());
    }

    public MenuDto fromMenuToMenuDto(Menu menu) {
        return new MenuDto(menu.getName(), menu.getDurationStart(), menu.getDurationEnd());
    }
}
