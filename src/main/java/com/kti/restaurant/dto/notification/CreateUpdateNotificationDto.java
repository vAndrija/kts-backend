package com.kti.restaurant.dto.notification;

import javax.validation.constraints.NotNull;

public class CreateUpdateNotificationDto {
	
	@NotNull(message = "Message should not be null or empty.")
    private String message;
	
	@NotNull(message = "Order Item id should not be null or empty.")
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
