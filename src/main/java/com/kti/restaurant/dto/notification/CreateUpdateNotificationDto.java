package com.kti.restaurant.dto.notification;

public class CreateUpdateNotificationDto {
    private String message;

    private Integer orderId;

    public CreateUpdateNotificationDto(String message, Integer orderId) {
        this.message = message;
        this.orderId = orderId;
    }

    public CreateUpdateNotificationDto() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
