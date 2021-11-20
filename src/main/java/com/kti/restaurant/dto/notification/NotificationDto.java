package com.kti.restaurant.dto.notification;


import com.kti.restaurant.dto.orderitem.NotificationOrderItemDto;
import com.kti.restaurant.dto.orderitem.OrderItemDto;

import javax.validation.constraints.NotNull;

public class NotificationDto {
    @NotNull(message = "Id should not be null.")
    private Integer id;

    @NotNull(message = "Message should not be null or empty.")
    private String message;

    @NotNull(message = "Order Item should not be empty.")
    private NotificationOrderItemDto orderItemDto;

    private Boolean seen;

    public NotificationDto() {

    }

    public NotificationDto(Integer id, String message, Boolean seen, NotificationOrderItemDto orderItemDto) {
        this.id = id;
        this.message = message;
        this.seen = seen;
        this.orderItemDto = orderItemDto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationOrderItemDto getOrderItemDto() {
        return orderItemDto;
    }

    public void setOrderItemDto(NotificationOrderItemDto orderItemDto) {
        this.orderItemDto = orderItemDto;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
