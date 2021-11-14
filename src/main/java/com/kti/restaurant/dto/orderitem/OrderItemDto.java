package com.kti.restaurant.dto.orderitem;

import com.kti.restaurant.model.enums.OrderItemStatus;

public class OrderItemDto {
    private Integer quantity;

    private String note;

    private OrderItemStatus status;

    private Integer priority;

    public OrderItemDto(Integer quantity, String note, OrderItemStatus status, Integer priority) {
        this.quantity = quantity;
        this.note = note;
        this.status = status;
        this.priority = priority;
    }

    public OrderItemDto(){

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
}
