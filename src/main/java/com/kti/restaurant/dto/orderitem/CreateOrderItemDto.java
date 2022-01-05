package com.kti.restaurant.dto.orderitem;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CreateOrderItemDto {
    @Min(value = 1, message = "Quantity should be greater than 0")
    private Integer quantity;

    private String note;

    @NotNull(message = "Status should not be null")
    private String status;

    @Min(value = 1,message = "Priority should be greater than 0")
    private Integer priority;

    @NotNull(message = "Order id should not be null")
    private Integer orderId;

    @NotNull(message = "Menu item id should not be null")
    private Integer menuItemId;

    public CreateOrderItemDto(Integer quantity, String note, String status, Integer priority,
                              Integer menuItemId, Integer orderId) {
        this.quantity = quantity;
        this.note = note;
        this.status = status;
        this.priority = priority;
        this.menuItemId = menuItemId;
        this.orderId = orderId;
    }

    public CreateOrderItemDto(){

    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Integer menuItemId) {
        this.menuItemId = menuItemId;
    }
}
