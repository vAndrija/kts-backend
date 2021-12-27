package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderItemService extends IService<OrderItem> {

    List<OrderItem> findOrderItemsInPeriod(LocalDateTime startDate, LocalDateTime endDate);
    List<OrderItem> findOrderItemsInPeriodForMenuItem(LocalDateTime startDate, LocalDateTime endDate, Integer id);
    List<OrderItem> findByCook(Integer cookId, LocalDateTime startDate, LocalDateTime endDate);
    List<OrderItem> findByBartender(Integer bartenderId, LocalDateTime startDate, LocalDateTime endDate);
    List<OrderItem> findByEmployee(Pageable page, Integer employeeId, HttpHeaders header) throws Exception;
    OrderItem updateStatus(Integer id, String status) throws Exception;
}
