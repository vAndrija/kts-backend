package com.kti.restaurant.dto.order;

import com.kti.restaurant.model.enums.OrderStatus;

import java.time.LocalDateTime;

public class UpdateOrderDto {
    private Integer id;

    private OrderStatus status;

    private LocalDateTime dateOfOrder;

    private Double price;
    // obrisati waiter jer mislim da nema potrebe
    private Integer waiter;

    public UpdateOrderDto(Integer id, OrderStatus status, LocalDateTime dateOfOrder, Double price) {
        this.id = id;
        this.status = status;
        this.dateOfOrder = dateOfOrder;
        this.price = price;
    }

    public UpdateOrderDto(Integer id, OrderStatus status, LocalDateTime dateOfOrder, Double price, Integer waiter) {
        this.id = id;
        this.status = status;
        this.dateOfOrder = dateOfOrder;
        this.price = price;
        this.waiter = waiter;
    }

    public UpdateOrderDto(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getWaiter() {
        return waiter;
    }

    public void setWaiter(Integer waiter) {
        this.waiter = waiter;
    }
}
