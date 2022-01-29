package com.kti.restaurant.dto.notification;

import javax.validation.constraints.NotNull;


public class NotificationDto {
    @NotNull(message = "Id should not be null.")
    private Integer id;

    @NotNull(message = "Message should not be null or empty.")
    private String message;

    private Integer orderItemId;
    
    public NotificationDto() {

    }

    public NotificationDto(Integer id, String message) {
        this.id = id;
        this.message = message;
    }
    public NotificationDto(Integer id, String message, Integer orderItemId) {
    	this.id = id;
    	this.message = message;
    	this.orderItemId = orderItemId;
    }
    
   	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
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
}
