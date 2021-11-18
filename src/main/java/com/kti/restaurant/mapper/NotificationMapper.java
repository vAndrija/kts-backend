package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.menuitem.MenuItemDto;
import com.kti.restaurant.dto.notification.CreateUpdateNotificationDto;
import com.kti.restaurant.dto.notification.NotificationDto;
import com.kti.restaurant.dto.orderitem.NotificationOrderItemDto;
import com.kti.restaurant.dto.orderitem.OrderItemDto;
import com.kti.restaurant.model.Notification;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.service.contract.IOrderItemService;
import com.kti.restaurant.service.implementation.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    private IOrderItemService orderItemService;
    private OrderItemMapper orderItemMapper;

    @Autowired
    public NotificationMapper(OrderItemService orderItemService, OrderItemMapper orderItemMapper) {
        this.orderItemService = orderItemService;
        this.orderItemMapper = orderItemMapper;
    }

    public Notification fromCreateNotificationDtoToNotification(CreateUpdateNotificationDto createNotificationDto) throws Exception {
        return new Notification(createNotificationDto.getMessage(),
                orderItemService.findById(createNotificationDto.getOrderItemId()), false);
    }

    public Notification fromUpdateNotificationDtoToNotification(CreateUpdateNotificationDto updateNotificationDto) throws Exception {
        return new Notification(updateNotificationDto.getMessage(),
                orderItemService.findById(updateNotificationDto.getOrderItemId()), true);
    }

    public NotificationDto fromNotificationToNotificationDto(Notification notification) {
        NotificationOrderItemDto orderItemDto = orderItemMapper.fromOrderItemToNotificationOrderItemDto(notification.getOrderItem());

        return new NotificationDto(notification.getId(), notification.getMessage(), notification.getSeen(), orderItemDto);
    }
}

