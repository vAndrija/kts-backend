package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.menuitem.MenuItemDto;
import com.kti.restaurant.dto.menuitem.UpdateMenuItemDto;
import com.kti.restaurant.dto.priceitem.PriceItemDto;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Menu;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.PriceItem;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.service.contract.IMenuService;
import com.kti.restaurant.service.contract.IPriceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MenuItemMapper {
    private IMenuService menuService;
    private IPriceItemService priceItemService;
    private PriceItemMapper priceItemMapper;

    @Autowired
    public MenuItemMapper(IMenuService menuService, IPriceItemService priceItemService, PriceItemMapper priceItemMapper) {
        this.menuService = menuService;
        this.priceItemService = priceItemService;
        this.priceItemMapper = priceItemMapper;
    }

    public MenuItem fromCreateMenuItemDtoToMenuItem(MenuItemDto menuItemDto) {
        return new MenuItem(menuItemDto.getName(), menuItemDto.getDescription(), MenuItemCategory.findCategory(menuItemDto.getCategory()),
                menuItemDto.getType(), menuItemDto.getPreparationTime());
    }

    public MenuItem fromUpdateMenuItemDtoToMenuItem(UpdateMenuItemDto menuItemDto) throws Exception {
        return new MenuItem(menuItemDto.getName(), menuItemDto.getDescription(), menuItemDto.getAccepted(),
                menuItemDto.getType(), menuItemDto.getCategory(), menuService.findById(menuItemDto.getMenuId()), menuItemDto.getPreparationTime());
    }

    public MenuItemDto fromMenuItemToMenuItemDto(MenuItem menuItem) {
        PriceItem priceItem = priceItemService.findPriceForDate(LocalDate.now(), menuItem.getId());
        PriceItemDto priceItemDto = priceItem != null ? priceItemMapper.fromPriceItemToPriceItemDto(priceItem): null;
        return new MenuItemDto(menuItem.getName(), menuItem.getDescription(), menuItem.getType(), menuItem.getCategory().getCategory(),
                menuItem.getPreparationTime(), priceItemDto);
    }
}
