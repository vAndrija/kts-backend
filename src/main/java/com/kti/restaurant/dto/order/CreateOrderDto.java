package com.kti.restaurant.dto.order;

import com.kti.restaurant.model.enums.OrderStatus;

import java.time.LocalDateTime;

public class CreateOrderDto {
    private OrderStatus status;

    private LocalDateTime dateOfOrder;

    private Double price;

    private Integer table;

    private Integer waiter;

    public CreateOrderDto(OrderStatus status, LocalDateTime dateOfOrder, Double price, Integer table, Integer waiter) {
        this.status = status;
        this.dateOfOrder = dateOfOrder;
        this.price = price;
        this.table = table;
        this.waiter = waiter;
    }

    public CreateOrderDto(OrderStatus status, LocalDateTime dateOfOrder, Double price, Integer table) {
        this.status = status;
        this.dateOfOrder = dateOfOrder;
        this.price = price;
        this.table = table;
    }

    public CreateOrderDto(){

    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(LocalDateTime dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getTable() {
        return table;
    }

    public void setTable(Integer table) {
        this.table = table;
    }

    public Integer getWaiter() {
        return waiter;
    }

    public void setWaiter(Integer waiter) {
        this.waiter = waiter;
    }
}
