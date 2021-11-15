package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.priceitem.CreatePriceItemDto;
import com.kti.restaurant.dto.priceitem.UpdatePriceItemDto;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.PriceItem;
import com.kti.restaurant.service.contract.IMenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class PriceItemMapper {
    private IMenuItemService menuItemService;

    @Autowired
    public PriceItemMapper(IMenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    public PriceItem fromCreatePriceItemDtoToPriceItem(CreatePriceItemDto priceItemDto) {
        MenuItem menuItem = findMenuItemById(priceItemDto.getMenuItemId());

        return new PriceItem(priceItemDto.getValue(), priceItemDto.getStartDate(), priceItemDto.getEndDate(),
                menuItem, priceItemDto.getCurrent());
    }

    @Valid
    public PriceItem fromUpdatePriceItemDtoToPriceItem(UpdatePriceItemDto priceItemDto) {
        MenuItem menuItem = findMenuItemById(priceItemDto.getMenuItemId());

        return new PriceItem(priceItemDto.getValue(), priceItemDto.getStartDate(), priceItemDto.getEndDate(),
                menuItem, priceItemDto.getCurrent(), priceItemDto.getId());
    }

    private MenuItem findMenuItemById(Integer id) {
        MenuItem menuItem = menuItemService.findById(id);

        if(menuItem == null) {
            throw new MissingEntityException("The menu item with given id does not exist in the system.");
        }

        return menuItem;
    }
}
