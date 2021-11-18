package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.discount.DiscountDto;
import com.kti.restaurant.model.Discount;
import com.kti.restaurant.service.contract.IMenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscountMapper {
    private IMenuItemService menuItemService;

    @Autowired
    public DiscountMapper(IMenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    public Discount fromDiscountDtoToDiscount(DiscountDto discountDto) throws Exception {
        return new Discount(discountDto.getValue(), discountDto.getStartDate(), discountDto.getEndDate(),
                discountDto.getCurrent(), menuItemService.findById(discountDto.getMenuItemId()));
    }

    public DiscountDto fromDiscountToDiscountDto(Discount discount) {
        return new DiscountDto(discount.getValue(), discount.getStartDate(), discount.getEndDate(),
                discount.getMenuItem().getId(), discount.getCurrent());
    }
}
