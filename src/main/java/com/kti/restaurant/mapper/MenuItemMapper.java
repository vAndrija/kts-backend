package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.menuitem.CreateMenuItemDto;
import com.kti.restaurant.dto.menuitem.UpdateMenuItemDto;
import com.kti.restaurant.model.Menu;
import com.kti.restaurant.model.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {
    public MenuItem fromCreateMenuItemDtoToMenuItem(CreateMenuItemDto menuItemDto) {
        return new MenuItem(menuItemDto.getName(), menuItemDto.getDescription(), menuItemDto.getCategory(),
                menuItemDto.getType());
    }

    //promeniti kada se dogovorimo sta cemo u vezi menija
    public MenuItem fromUpdateMenuItemDtoToMenuItem(UpdateMenuItemDto menuItemDto) {
        return new MenuItem(menuItemDto.getId(), menuItemDto.getName(), menuItemDto.getDescription(), menuItemDto.getAccepted(),
                menuItemDto.getType(), menuItemDto.getCategory(), new Menu());
    }
}
