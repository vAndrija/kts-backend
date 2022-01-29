package com.kti.restaurant.dto.notification;

import javax.validation.constraints.NotNull;

public class CreateNotificationDto {
	
	@NotNull(message = "Message should not be null or empty.")
    private String message;
	
	private Integer orderItemId;

    public CreateNotificationDto(String message) {
        this.message = message;
    }
    
    public CreateNotificationDto(String message, Integer orderItemId) {
        this.message = message;
        this.orderItemId = orderItemId;
    }

    public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}

	public CreateNotificationDto() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    } 
}
