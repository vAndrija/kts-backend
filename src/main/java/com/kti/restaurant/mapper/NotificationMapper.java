package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.notification.CreateNotificationDto;
import com.kti.restaurant.dto.notification.NotificationDto;
import com.kti.restaurant.model.Notification;
import com.kti.restaurant.service.contract.IOrderItemService;
import com.kti.restaurant.service.implementation.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    private IOrderItemService orderItemService;

    @Autowired
    public NotificationMapper(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    public Notification fromCreateNotificationDtoToNotification(CreateNotificationDto createNotificationDto) throws Exception {
    	if(createNotificationDto.getOrderItemId() == -1) {
    		return new Notification(createNotificationDto.getMessage(), null);
    	}
    	return new Notification(createNotificationDto.getMessage(), orderItemService.findById(createNotificationDto.getOrderItemId()));
    	
    }

    public NotificationDto fromNotificationToNotificationDto(Notification notification) {
        return new NotificationDto(notification.getId(), notification.getMessage());
    }
}

