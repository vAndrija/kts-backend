package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.priceitem.PriceItemDto;
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

    public PriceItem fromPriceItemDtoToPriceItem(PriceItemDto priceItemDto) throws Exception {
        MenuItem menuItem = menuItemService.findById(priceItemDto.getMenuItemId());

        return new PriceItem(priceItemDto.getValue(), priceItemDto.getStartDate(), priceItemDto.getEndDate(),
                menuItem, priceItemDto.getCurrent());
    }
}
