package com.kti.restaurant.dto.orderitem;

import com.kti.restaurant.dto.menuitem.MenuItemDto;
import com.kti.restaurant.model.enums.OrderItemStatus;

public class NotificationOrderItemDto {
    private Integer id;

    private String note;

    private Integer quantity;

    private OrderItemStatus status;

    private MenuItemDto menuItemDto;

    public NotificationOrderItemDto() {

    }

    public NotificationOrderItemDto(Integer id, String note, Integer quantity, OrderItemStatus status, MenuItemDto menuItemDto) {
        this.id = id;
        this.note = note;
        this.quantity = quantity;
        this.status = status;
        this.menuItemDto = menuItemDto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public MenuItemDto getMenuItemDto() {
        return menuItemDto;
    }

    public void setMenuItemDto(MenuItemDto menuItemDto) {
        this.menuItemDto = menuItemDto;
    }
}
