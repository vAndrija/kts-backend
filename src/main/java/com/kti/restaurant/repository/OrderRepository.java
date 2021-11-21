package com.kti.restaurant.repository;

import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    Collection<Order> findOrderByStatus(OrderStatus status);
}
