package com.kti.restaurant.dto.orderitem;

import com.kti.restaurant.model.enums.OrderItemStatus;

public class UpdateOrderItemDto {
    private Integer id;

    private Integer quantity;

    private String note;

    private OrderItemStatus status;

    private Integer priority;

    private Integer order;
    //mislim da mi menuitem ne treba
    private Integer menuItem;

    private Integer bartender;

    private Integer cook;
    //dodati cook barteneder i order
    public UpdateOrderItemDto(Integer id, Integer quantity, String note, OrderItemStatus status, Integer priority) {
        this.id = id;
        this.quantity = quantity;
        this.note = note;
        this.status = status;
        this.priority = priority;
    }

    public UpdateOrderItemDto(){

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

    public Integer getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(Integer menuItem) {
        this.menuItem = menuItem;
    }

    public Integer getBartender() {
        return bartender;
    }

    public void setBartender(Integer bartender) {
        this.bartender = bartender;
    }

    public Integer getCook() {
        return cook;
    }

    public void setCook(Integer cook) {
        this.cook = cook;
    }
}
