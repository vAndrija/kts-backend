package com.kti.restaurant.repository;

import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("select o from Order o where o.waiter.id=?1 and o.status=?2")
    Page<Order> findByWaiterAndStatus(Integer id, OrderStatus status, Pageable pageable);

    @Query("select o from Order o where o.waiter.id=?1")
    Page<Order> findByWaiter(Integer id, Pageable pageable);
}
