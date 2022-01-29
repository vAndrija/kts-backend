package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface IOrderService extends IService<Order> {

    Page<Order> filterByStatus(Integer id, String status, Pageable pageable) throws Exception;

    Page<Order> findByWaiter(Integer id, Pageable pageable) throws Exception;

    Order updateStatus(Integer id, String status) throws Exception;

    List<Order> findByRestaurantTable(Integer id) throws Exception;
}
