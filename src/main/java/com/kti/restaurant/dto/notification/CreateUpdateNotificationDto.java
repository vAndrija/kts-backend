package com.kti.restaurant.dto.notification;

public class CreateUpdateNotificationDto {
    private String message;

    private Integer orderItemId;

    public CreateUpdateNotificationDto(String message, Integer orderItemId) {
        this.message = message;
        this.orderItemId = orderItemId;
    }

    public CreateUpdateNotificationDto() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }
}
