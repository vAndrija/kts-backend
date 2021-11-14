package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.notification.CreateUpdateNotificationDto;
import com.kti.restaurant.model.Notification;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    //treba dodati servis za OrderItem
    public Notification fromCreateNotificationDtoToNotification(CreateUpdateNotificationDto createNotificationDto) {
        return new Notification(createNotificationDto.getMessage(), new OrderItem(), false);
    }

    public Notification fromUpdateNotificationDtoToNotification(CreateUpdateNotificationDto updateNotificationDto) {
        return new Notification(updateNotificationDto.getMessage(), new OrderItem(), true);
    }

}
