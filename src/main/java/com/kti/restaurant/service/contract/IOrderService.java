package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.Order;

import java.util.Collection;

public interface IOrderService extends IService<Order>{

    Collection<Order> filterByStatus(String status) throws Exception;
}
