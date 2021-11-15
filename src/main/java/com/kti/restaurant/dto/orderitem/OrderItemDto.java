package com.kti.restaurant.dto.orderitem;

import com.kti.restaurant.model.enums.OrderItemStatus;

public class OrderItemDto {
    private Integer id;

    private Integer quantity;

    private String note;

    private OrderItemStatus status;

    private Integer priority;

    private Integer order;

    public OrderItemDto(Integer id, Integer quantity, String note, OrderItemStatus status, Integer priority,
                        Integer order) {
        this.id = id;
        this.quantity = quantity;
        this.note = note;
        this.status = status;
        this.priority = priority;
        this.order = order;
    }

    public OrderItemDto(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
