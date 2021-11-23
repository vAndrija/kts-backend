package com.kti.restaurant.dto.report;

import com.kti.restaurant.dto.menuitem.ReportMenuItemDto;

public class MenuItemSalesDto {
    private Integer quantity;
    private ReportMenuItemDto menuItemDto;

    public MenuItemSalesDto() {

    }

    public MenuItemSalesDto(Integer quantity, ReportMenuItemDto menuItemDto) {
        this.quantity = quantity;
        this.menuItemDto = menuItemDto;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ReportMenuItemDto getMenuItemDto() {
        return menuItemDto;
    }

    public void setMenuItemDto(ReportMenuItemDto menuItemDto) {
        this.menuItemDto = menuItemDto;
    }
}
