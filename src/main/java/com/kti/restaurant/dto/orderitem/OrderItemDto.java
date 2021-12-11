package com.kti.restaurant.dto.orderitem;

import com.kti.restaurant.model.enums.OrderItemStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderItemDto {
    @NotNull(message = "Id should not be null.")
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

    @NotNull(message = "MenuItem id should not be null or empty")
    private Integer menuItemId;

    private Integer bartenderId;

    private Integer cookId;

    public OrderItemDto(Integer id, Integer quantity, String note, OrderItemStatus status, Integer priority,
                        Integer orderId, Integer menuItemId,  Integer bartenderId,  Integer cookId) {
        this.id = id;
        this.quantity = quantity;
        this.note = note;
        this.status = status;
        this.priority = priority;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.bartenderId = bartenderId;
        this.cookId = cookId;
    }
    public OrderItemDto(Integer id, Integer quantity, String note, OrderItemStatus status, Integer priority,
                        Integer orderId, Integer menuItemId) {
        this.id = id;
        this.quantity = quantity;
        this.note = note;
        this.status = status;
        this.priority = priority;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getMenuItemId() { return menuItemId; }

    public void setMenuItemId(Integer menuItemId) { this.menuItemId = menuItemId; }

    public Integer getBartenderId() { return bartenderId;}

    public void setBartenderId(Integer bartenderId) { this.bartenderId = bartenderId; }

    public Integer getCookId() { return cookId; }

    public void setCookId(Integer cookId) { this.cookId = cookId; }
}
