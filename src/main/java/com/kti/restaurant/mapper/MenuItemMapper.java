package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.menuitem.MenuItemDto;
import com.kti.restaurant.dto.menuitem.UpdateMenuItemDto;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Menu;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.service.contract.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {
    private IMenuService menuService;

    @Autowired
    public MenuItemMapper(IMenuService menuService) {
        this.menuService = menuService;
    }

    public MenuItem fromCreateMenuItemDtoToMenuItem(MenuItemDto menuItemDto) {
        return new MenuItem(menuItemDto.getName(), menuItemDto.getDescription(), menuItemDto.getCategory(),
                menuItemDto.getType(), menuItemDto.getPreparationTime());
    }

    public MenuItem fromUpdateMenuItemDtoToMenuItem(UpdateMenuItemDto menuItemDto) throws Exception {
        return new MenuItem(menuItemDto.getName(), menuItemDto.getDescription(), menuItemDto.getAccepted(),
                menuItemDto.getType(), menuItemDto.getCategory(), menuService.findById(menuItemDto.getMenuId()), menuItemDto.getPreparationTime());
    }

    public MenuItemDto fromMenuItemToMenuItemDto(MenuItem menuItem) {
        return new MenuItemDto(menuItem.getName(), menuItem.getDescription(), menuItem.getType(), menuItem.getCategory());
    }
}
