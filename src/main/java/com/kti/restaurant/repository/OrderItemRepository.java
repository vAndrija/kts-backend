package com.kti.restaurant.repository;

import com.kti.restaurant.model.Bartender;
import com.kti.restaurant.model.Cook;
import com.kti.restaurant.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
    @Query("select oi from OrderItem oi join Order o on oi.order.id = o.id where o.dateOfOrder > ?1 and o.dateOfOrder < ?2")
    List<OrderItem> findOrderItemsByDate(LocalDateTime startDate, LocalDateTime endDate);

    @Query("select oi from OrderItem oi join Order o on oi.order.id = o.id join MenuItem mi on oi.menuItem.id = mi.id " +
            "where o.dateOfOrder > ?1 and o.dateOfOrder < ?2 and mi.id = ?3")
    List<OrderItem> findSalesForMenuItem(LocalDateTime startDate, LocalDateTime endDate, Integer menuItemId);
  
    @Query("select oi from OrderItem oi join Order o on oi.order.id = o.id where o.dateOfOrder > ?2 and o.dateOfOrder < ?3 and oi.cook = ?1")
    List<OrderItem> findByCookForDate(Cook cook, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select oi from OrderItem oi join Order o on oi.order.id = o.id where o.dateOfOrder > ?2 and o.dateOfOrder < ?3 and oi.bartender = ?1")
    List<OrderItem> findByBartenderForDate(Bartender bartender, LocalDateTime startDate, LocalDateTime endDate);
}
