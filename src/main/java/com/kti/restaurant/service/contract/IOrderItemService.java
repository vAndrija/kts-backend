package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderItemService extends IService<OrderItem> {

    List<OrderItem> findOrderItemsInPeriod(LocalDateTime startDate, LocalDateTime endDate);
}
