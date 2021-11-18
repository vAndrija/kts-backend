package com.kti.restaurant.dto.orderitem;

import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.model.enums.OrderItemStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderItemDto {
    private Integer id;

    @NotEmpty(message = "Quantity should not be null or empty")
    private Integer quantity;

    @NotEmpty(message = "Note should not be null or empty")
    private String note;

    @NotNull(message = "Status should not be null")
    private OrderItemStatus status;

    @NotEmpty(message = "Priority should not be null or empty")
    private Integer priority;

    @NotNull(message = "Order id should not be null or empty")
    private Integer orderId;

    public OrderItemDto(Integer id, Integer quantity, String note, OrderItemStatus status, Integer priority,
                        Integer orderId) {
        this.id = id;
        this.quantity = quantity;
        this.note = note;
        this.status = status;
        this.priority = priority;
        this.orderId = orderId;
    }

    public OrderItemDto(){

    }

    public Integer gerId() {
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
