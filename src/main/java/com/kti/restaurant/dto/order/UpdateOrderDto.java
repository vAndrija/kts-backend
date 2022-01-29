package com.kti.restaurant.dto.order;

import com.kti.restaurant.model.enums.OrderStatus;

import javax.validation.constraints.NotNull;

public class UpdateOrderDto {
    @NotNull(message = "Status should not be null")
    private OrderStatus status;

    @NotNull(message = "Price should not be null")
    private Double price;

    public UpdateOrderDto( OrderStatus status, Double price) {
        this.status = status;
        this.price = price;
    }

    public UpdateOrderDto(){

    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
