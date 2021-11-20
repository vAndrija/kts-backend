package com.kti.restaurant.repository;

import com.kti.restaurant.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
    @Query("select oi from OrderItem oi join Order o on oi.order.id = o.id where o.dateOfOrder > ?1 and o.dateOfOrder < ?2")
    List<OrderItem> findOrderItemsByDate(LocalDateTime startDate, LocalDateTime endDate);
}
