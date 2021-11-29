package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.Bartender;
import com.kti.restaurant.model.Cook;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderItemService extends IService<OrderItem> {

    List<OrderItem> findOrderItemsInPeriod(LocalDateTime startDate, LocalDateTime endDate);
    List<OrderItem> findByCook(Cook cook, LocalDateTime startDate, LocalDateTime endDate);
    List<OrderItem> findByBartender(Bartender bartender, LocalDateTime startDate, LocalDateTime endDate);
}
