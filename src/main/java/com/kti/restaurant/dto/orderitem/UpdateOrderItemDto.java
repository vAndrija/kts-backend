package com.kti.restaurant.dto.orderitem;

import com.kti.restaurant.model.enums.OrderItemStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateOrderItemDto {
    @NotNull(message = "Quantity should not be null or empty")
    private Integer quantity;

    @NotEmpty(message = "Note should not be null or empty")
    private String note;

    @NotNull(message = "Status should not be null")
    private OrderItemStatus status;

    @NotNull(message = "Priority should not be null or empty")
    private Integer priority;

    @NotNull(message = "Menu item id should not be null or empty")
    private Integer menuItemId;

    private Integer bartenderId;

    private Integer cookId;

    //dodati cook barteneder
    public UpdateOrderItemDto( Integer quantity, String note, OrderItemStatus status, Integer priority,Integer menuItemId
                               ) {
        this.quantity = quantity;
        this.note = note;
        this.status = status;
        this.priority = priority;
        this.menuItemId = menuItemId;
    }

    public UpdateOrderItemDto(){

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

    public Integer getBartenderId() {
        return bartenderId;
    }

    public void setBartenderId(Integer bartenderId) {
        this.bartenderId = bartenderId;
    }

    public Integer getCookId() {
        return cookId;
    }

    public void setCookId(Integer cookId) {
        this.cookId = cookId;
    }

   public Integer getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Integer menuItemId) {
        this.menuItemId = menuItemId;
    }
}
