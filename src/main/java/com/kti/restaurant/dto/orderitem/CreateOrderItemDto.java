package com.kti.restaurant.dto.orderitem;

import com.kti.restaurant.model.enums.OrderItemStatus;

public class CreateOrderItemDto {
    private Integer quantity;

    private String note;

    private OrderItemStatus status;

    private Integer priority;

    private Integer order;

    private Integer menuItem;

    //dodati i order
    public CreateOrderItemDto(Integer quantity, String note, OrderItemStatus status, Integer priority, Integer menuItem) {
        this.quantity = quantity;
        this.note = note;
        this.status = status;
        this.priority = priority;
        this.menuItem = menuItem;
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

    public Integer getOrder() {return order;}

    public void setOrder(Integer order) {this.order = order;}

    public Integer getMenuItem() {return menuItem;}

    public void setMenuItem(Integer menuItem) {this.menuItem = menuItem;}
}
