package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.OrderItem;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderItemService extends IService<OrderItem> {

    List<OrderItem> findOrderItemsInPeriod(LocalDateTime startDate, LocalDateTime endDate);
    List<OrderItem> findOrderItemsInPeriodForMenuItem(LocalDateTime startDate, LocalDateTime endDate, Integer id);
}
