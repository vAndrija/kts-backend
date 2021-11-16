package com.kti.restaurant.dto.order;

import com.kti.restaurant.model.enums.OrderStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateOrderDto {
    @NotNull(message = "Status should not be null")
    private OrderStatus status;

    @NotNull(message = "Date of order should not be null")
    private LocalDateTime dateOfOrder;

    @NotNull(message = "Price should not be null")
    private Double price;

    @NotNull(message = "Table id should not be null")
    private Integer tableId;

    private Integer waiterId;

    public CreateOrderDto(OrderStatus status, LocalDateTime dateOfOrder, Double price, Integer tableId, Integer waiterId) {
        this.status = status;
        this.dateOfOrder = dateOfOrder;
        this.price = price;
        this.tableId = tableId;
        this.waiterId = waiterId;
    }

    public CreateOrderDto(OrderStatus status, LocalDateTime dateOfOrder, Double price, Integer tableId) {
        this.status = status;
        this.dateOfOrder = dateOfOrder;
        this.price = price;
        this.tableId = tableId;
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

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(Integer waiterId) {
        this.waiterId = waiterId;
    }
}
