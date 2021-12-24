package com.kti.restaurant.repository;

import com.kti.restaurant.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  
    @Query("select oi from OrderItem oi join Order o on oi.order.id = o.id where o.dateOfOrder > ?2 and o.dateOfOrder < ?3 and oi.cook.id = ?1")
    List<OrderItem> findByCookForDate(Integer cookId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select oi from OrderItem oi join Order o on oi.order.id = o.id where o.dateOfOrder > ?2 and o.dateOfOrder < ?3 and oi.bartender.id = ?1")
    List<OrderItem> findByBartenderForDate(Integer bartenderId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select oi from OrderItem oi join Order o on oi.order.id = o.id where oi.bartender.id = ?1 or oi.cook.id=?1")
    Page<OrderItem> findByEmployee(Pageable pageable, Integer employeeId);
}
