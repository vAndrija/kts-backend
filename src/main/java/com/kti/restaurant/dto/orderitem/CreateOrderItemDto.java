package com.kti.restaurant.dto.orderitem;

import com.kti.restaurant.model.enums.OrderItemStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateOrderItemDto {
    @NotNull(message = "Quantity should not be null")
    private Integer quantity;

    @NotEmpty(message = "Note should not be null or empty")
    private String note;

    @NotNull(message = "Status should not be null")
    private OrderItemStatus status;

    @NotNull(message = "Priority should not be null")
    private Integer priority;

    @NotNull(message = "Order id should not be null")
    private Integer orderId;

    @NotNull(message = "Menu item id should not be null")
    private Integer menuItemId;

    public CreateOrderItemDto(Integer quantity, String note, OrderItemStatus status, Integer priority,
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

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
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
